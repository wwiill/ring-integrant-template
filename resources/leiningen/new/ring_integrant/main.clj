(ns {{name}}.main
  (:require
    [{{name}}.app :as app]
    [{{name}}.logging :as log]
    [{{name}}.server :as server]
    [{{name}}.store :as store]
    [integrant.core :as ig])
  (:gen-class))

(defn config []
  {::app/handler  {:db (ig/ref ::store/db)}
   ::server/jetty {:port    (or (System/getenv "PORT") "9000")
                   :handler (ig/ref ::app/handler)}
   ::store/db     {}})

(defn system []
  (ig/init (config)))

(defn -main [& _]
  (log/debug nil "Starting system")
  (try
    (system)
    (catch Throwable e
      (log/error nil e "Error starting or stopping system")
      (System/exit 1))))