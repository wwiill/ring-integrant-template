(ns {{name}}.server
  (:require
    [{{name}}.logging :as log]

    [integrant.core :as ig]
    [ring.adapter.jetty :refer [run-jetty]]))

(defmethod ig/init-key ::jetty
  [_ {:keys [port handler]}]
  (log/info {:port port} "Starting jetty")

  (run-jetty
    handler
    {:join?                false
     :send-server-version? false
     :port                 (Integer/parseInt port)}))

(defmethod ig/halt-key! ::jetty
  [_ jetty]
  (log/info nil "Halting jetty")
  (when jetty
    (.stop jetty))
  nil)

