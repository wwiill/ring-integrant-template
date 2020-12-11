(ns {{name}}.app
  (:require
    [{{name}}.logging :as log]
    [integrant.core :as ig]
    [reitit.core :as reitit]
    [reitit.ring :as reitit-ring]
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]))

(defn router [db]
  (reitit-ring/router
    [["/api"
      ["/swagger.json"
       {:get {:handler (swagger/create-swagger-handler)}}]]]))

(defmethod ig/init-key ::handler
 [_ {:keys [db]}]
 (log/info nil "Initialising handler")
 (reitit-ring/ring-handler
   (router db)
   (reitit-ring/routes (swagger-ui/create-swagger-ui-handler {:path "/api" :url "/api/swagger.json"}))
   (reitit-ring/create-default-handler
     {:not-found (constantly {:status 404 :body "Not found"})})))

(defmethod ig/halt-key! ::handler
 [_ handler]
 nil)
