(ns {{name}} .main
  (:require
    [{{name}} .logging :as log]
    [{{name}} .routes :as routes]
    [{{name}} .http :as http]
    [{{name}} .store :as store]

    [integrant.core :as ig])
  (:gen-class))

(defn config []
  {::routes/handler {}
   ::http/jetty     {:port    (or (System/getenv "PORT") "9000")
                     :handler (ig/ref ::routes/handler)}
   ::store/db       {}})

(defn system []
  (ig/init (config)))

(defn -main [& _]
  (log/debug nil "Starting system")
  (try
    (system)
    (catch Throwable e
      (log/error nil e "Error starting or stopping system")
      (System/exit 1))))