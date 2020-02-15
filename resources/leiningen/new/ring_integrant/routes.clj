(ns {{name}}.routes
  (:require
    [{{name}}.logging :as log]

    [integrant.core :as ig]
    [ring.middleware.params :as params]
    [ring.middleware.keyword-params :refer [wrap-keyword-params]]
    [ring.middleware.format :refer [wrap-restful-format]]
    [compojure.core :refer [routes GET POST]]))

(defn app-routes [db]
  (routes
    (GET "/_system/health" [] {:status 200 :body {:message "System healthy"}})
    (GET "/things" [] {:status 200 :body []})
    (POST "/things" [] {:status 200})))

(defmethod ig/init-key ::handler
  [_ {:keys [db]}]
  (log/info nil "Initialising handler")
  (-> (app-routes db)
      (params/wrap-params)
      (wrap-keyword-params)
      (wrap-restful-format :formats [:transit-json :json :edn])))

(defmethod ig/halt-key! ::handler
  [_ handler]
  nil)
