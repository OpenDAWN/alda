(ns alda.server
  (:require [alda.now                 :as    now]
            [alda.lisp                :refer :all]
            [alda.parser-util         :refer (parse-with-context)]
            [ring.middleware.defaults :refer (wrap-defaults api-defaults)]
            [ring.adapter.jetty       :refer (run-jetty)]))

(defn start-alda-environment!
  []
  ; set up audio generators
  (now/set-up! :midi)
  ; initialize a new score
  (score*))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- success
  [body]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    body})

(defn- user-error
  [body]
  {:status  400
   :headers {"Content-Type" "text/html"}
   :body    body})

(defn- system-error
  [body]
  {:status  500
   :headers {"Content-Type" "text/html"}
   :body    body})

(defn handler
  [{:keys [params] :as request}]
  (try
    (let [alda-code (:code params)
          [context parse-result] (parse-with-context alda-code)]
      (if (= context :parse-failure)
        (user-error "Invalid Alda syntax.")
        (do
          (now/play! (eval (case context
                             :music-data (cons 'do parse-result)
                             :score (cons 'do (rest parse-result))
                             parse-result)))
          (success "OK"))))
    (catch Throwable e
      (system-error (str "ERROR: " (.getMessage e))))))

(def app
  (wrap-defaults handler api-defaults))

(defn start-server!
  [port]
  (start-alda-environment!)
  (run-jetty app {:port port}))

