(ns avro-registry.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [avro-registry.store :as store]
            [liberator.core :refer [resource defresource]])
  )

(defroutes app-routes
           (GET "/ping" [] (resource :available-media-types ["text/plain"]
                                     :handle-ok "Pong"))

           (GET "/" [] (resource :available-media-types ["application/json"]
                                 :handle-ok (store/get-subjects-list)))

           (PUT "/:subject" [subject] (store/create-subject subject))
           (PUT "/:subject/register" {{subject :subject} :params body :body} (store/register-schema! subject body))

           (GET "/:subject/all" [subject]
             (resource :available-media-types ["application/json"]
                       :handle-ok (store/get-all-schemas subject)))

           (GET "/:subject/config" [subject] (store/get-config subject))

           (GET "/:subject/latest" [subject] (resource :available-media-types ["application/json"]
                                                       :handle-ok (store/get-latest-schema subject)))

           (GET "/:subject/id/:id" [subject id] (resource :available-media-types ["application/json"]
                                                          :handle-ok (store/get-schema subject id))))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body {:keywords? false})
      (middleware/wrap-json-response)))
