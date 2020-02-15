(ns dev
  (:require
    [{{name}}.store :as store]
    [{{name}}.routes :as routes]
    [{{name}}.server :as server]
    [{{name}}.main :as main]
    [integrant.repl :refer [set-prep! clear go halt prep init reset reset-all] :as ig-repl]
    [integrant.core :as ig]))

(defn dev-config []
  {::routes/handler {}
   ::server/jetty   {:port    (or (System/getenv "PORT") "9000")
                     :handler (ig/ref ::routes/handler)}
   ::store/db       {}})

(ig-repl/set-prep! dev-config)
