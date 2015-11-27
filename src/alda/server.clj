(ns alda.server
  (:require [alda.now                 :as    now]
            [alda.lisp                :refer :all]
            [alda.parser-util         :refer (parse-with-context)]
            [alda.sound               :refer (*play-opts*)]
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
  (POST "/" {:keys [play-opts params] :as request}
    (try
      (let [{:keys [code]} params
            [context parse-result] (parse-with-context code)]
        (if (= context :parse-failure)
          (user-error "Invalid Alda syntax.")
          (binding [*play-opts* play-opts]
            (now/play! (eval (case context
                               :music-data (cons 'do parse-result)
                               :score (cons 'do (rest parse-result))
                               parse-result)))
            (success "OK"))))
      (catch Throwable e
        (server-error (str "ERROR: " (.getMessage e))))))
  (not-found "Invalid route."))

(defn wrap-play-opts
  [next-handler play-opts]
  (fn [request]
    (next-handler (assoc request :play-opts play-opts))))

(def app
  (-> (wrap-defaults server-routes api-defaults)
      (wrap-play-opts *play-opts*)))

(defn start-server!
  [port]
  (start-alda-environment!)
  (run-jetty app {:port port}))

