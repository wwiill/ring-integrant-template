(ns dev
  (:require
    [{{name}}.app :as app]
    [{{name}}.main :as main]
    [{{name}}.server :as server]
    [{{name}}.store :as store]

    [integrant.core :as ig]
    [integrant.repl :refer [set-prep! clear go halt prep init reset reset-all] :as ig-repl]))

(defn dev-config []
  {::app/handler  {:db (ig/ref ::store/db)}
   ::server/jetty {:port    (or (System/getenv "PORT") "9000")
                   :handler (ig/ref ::app/handler)}
   ::store/db     {}})

(ig-repl/set-prep! dev-config)
