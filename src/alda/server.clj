(ns alda.server
  (:require [alda.now                 :as    now]
            [alda.lisp                :refer :all]
            [alda.parser-util         :refer (parse-with-context)]
            [ring.middleware.defaults :refer (wrap-defaults api-defaults)]
            [ring.adapter.jetty       :refer (run-jetty)]
            [compojure.core           :refer :all]
            [compojure.route          :refer (not-found)]))

(defn start-alda-environment!
  []
  ; set up audio generators
  (now/set-up! :midi)
  ; initialize a new score
  (score*))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- response
  [code]
  (fn [body]
    {:status  code
     :headers {"Content-Type" "text/html"}
     :body    body}))

(def ^:private success      (response 200))
(def ^:private user-error   (response 400))
(def ^:private server-error (response 500))

(defroutes server-routes
  (GET "/" []
    (str (score-map)))
  (POST "/" [code]
    (try
      (let [[context parse-result] (parse-with-context code)]
        (if (= context :parse-failure)
          (user-error "Invalid Alda syntax.")
          (do
            (now/play! (eval (case context
                               :music-data (cons 'do parse-result)
                               :score (cons 'do (rest parse-result))
                               parse-result)))
            (success "OK"))))
      (catch Throwable e
        (server-error (str "ERROR: " (.getMessage e))))))
  (not-found "Invalid route."))

(def app
  (wrap-defaults server-routes api-defaults))

(defn start-server!
  [port]
  (start-alda-environment!)
  (run-jetty app {:port port}))

