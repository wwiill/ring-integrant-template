(ns {{name}}.store
  (:require
    [{{name}}.logging :as log]
    [integrant.core :as ig]))

(defmethod ig/init-key ::db
  [_ db-config]
  (log/info nil "Starting db"))

(defmethod ig/halt-key! ::db
  [_ db]
  (log/info nil "Halting db")
  nil)
