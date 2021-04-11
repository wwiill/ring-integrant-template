(ns {{name}}.app
  (:require
    [{{name}}.logging :as log]
    [integrant.core :as ig]
    [malli.util :as mu]
    [muuntaja.core :as m]
    [reitit.coercion.malli :as coercion-malli]
    [reitit.dev.pretty :as pretty]
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]
    [reitit.ring.coercion :as coercion]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.exception :as exception]
    [reitit.ring.middleware.parameters :as parameters]
    [reitit.ring :as reitit-ring]))

(defn router [db]
  (reitit-ring/router
    ["/api"
     {:swagger    {:id :my-app}
      :middleware [swagger/swagger-feature]}

     ["/swagger.json"
      {:get {:no-doc  true
             :swagger {:info {:title "my-api"}}
             :handler (swagger/create-swagger-handler)}}]]

    {;;:reitit.middleware/transform dev/print-request-diffs ;; pretty diffs
     ;;:validate spec/validate ;; enable spec validation for route data
     ;;:reitit.spec/wrap spell/closed ;; strict top-level validation
     :exception pretty/exception
     :data      {:coercion   (coercion-malli/create
                               {;; set of keys to include in error messages
                                :error-keys       #{#_:type :coercion :in :schema :value :errors :humanized #_:transformed}
                                ;; schema identity function (default: close all map schemas)
                                :compile          mu/closed-schema
                                ;; strip-extra-keys (effects only predefined transformers)
                                :strip-extra-keys true
                                ;; add/set default values
                                :default-values   true
                                ;; malli options
                                :options          nil})
                 :muuntaja   m/instance
                 :middleware [;; swagger feature
                              swagger/swagger-feature
                              ;; query-params & form-params
                              parameters/parameters-middleware
                              ;; content-negotiation
                              muuntaja/format-negotiate-middleware
                              ;; encoding response body
                              muuntaja/format-response-middleware
                              ;; exception handling
                              exception/exception-middleware
                              ;; decoding request body
                              muuntaja/format-request-middleware
                              ;; coercing response bodys
                              coercion/coerce-response-middleware
                              ;; coercing request parameters
                              coercion/coerce-request-middleware]}}))

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