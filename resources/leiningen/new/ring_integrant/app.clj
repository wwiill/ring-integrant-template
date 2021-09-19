(ns {{name}}.app
  (:require
    [{{name}}.logging :as log]
    [integrant.core :as ig]
    [malli.util]
    [muuntaja.core]
    [reitit.coercion.malli :as coercion.malli]
    [reitit.dev.pretty :as pretty]
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]
    [reitit.ring.coercion :as coercion]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.exception :as exception]
    [reitit.ring.middleware.parameters :as parameters]
    [reitit.ring]))

(defn router [db]
  (reitit.ring/router
    ["/api"
     {:swagger    {:id :my-app}
      :middleware [swagger/swagger-feature]}

     ["/swagger.json"
      {:get {:no-doc  true
             :swagger {:info {:title "my-api"}}
             :handler (swagger/create-swagger-handler)}}]

     ["/plus"
      {:get {:parameters {:query [:map [:a int?] [:b int?]]}
             :handler (fn [req] {:status 200
                                 :body {:data (+ (-> req :parameters :query :a)
                                                 (-> req :parameters :query :b))}})
             :responses {200 {:body [:map [:data int?]]}}}}]]

    {;;:reitit.middleware/transform dev/print-request-diffs ;; pretty diffs
     :exception pretty/exception
     :data {:coercion (coercion.malli/create
                        {;; set of keys to include in error messages
                         :error-keys #{#_:type :coercion :in :schema :value :errors :humanized #_:transformed}
                         ;; schema identity function (default: close all map schemas)
                         :compile malli.util/closed-schema
                         ;; strip-extra-keys (effects only predefined transformers)
                         :strip-extra-keys false
                         ;; add/set default values
                         :default-values true
                         ;; malli options
                         :options nil})
            :muuntaja muuntaja.core/instance
            :middleware [;; middleware are executed from top to bottom
                         ;; (swagger-feature (format-negotiate-middleware ... (my-route-middleware (my-handler request))

                         ;; swagger feature
                         swagger/swagger-feature
                         ;; content-negotiation
                         muuntaja/format-negotiate-middleware
                         ;; encoding response body
                         muuntaja/format-response-middleware
                         ;; exception handling
                         exception/exception-middleware
                         ;; coercing response body
                         coercion/coerce-response-middleware

                         ;; adds :params, :query-params & :form-params
                         parameters/parameters-middleware
                         ;; decoding request body
                         muuntaja/format-request-middleware
                         ;; adds :parameters with coerced :path, :body and :query maps
                         coercion/coerce-request-middleware

                         ;; now hand off to route middleware and then to route handler
                         ]}}))

(defmethod ig/init-key ::handler [_ {:keys [db]}]
 (log/info nil "Initialising handler")
 (reitit.ring/ring-handler
   (router db)
   (reitit.ring/routes

     (reitit.ring/redirect-trailing-slash-handler 
       {:method :strip})

     (swagger-ui/create-swagger-ui-handler
       {:path "/api" :url "/api/swagger.json"})

     (reitit.ring/create-default-handler
       {:not-found          (constantly {:status 404, :body "not-found"})
        :method-not-allowed (constantly {:status 405, :body "not-allowed"})
        :not-acceptable     (constantly {:status 406, :body "not-acceptable"})}))))

(defmethod ig/halt-key! ::handler
 [_ handler]
 nil)