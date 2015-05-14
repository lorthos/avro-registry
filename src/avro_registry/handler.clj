(ns avro-registry.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [compojure.route :as route]
            [avro-registry.store :as store])
  )

(defroutes app-routes
           (GET "/ping" [] "pong")
           (GET "/" [] (store/get-subjects-list))
           (PUT "/:subject" [subject] (store/create-subject subject))
           (PUT "/:subject/register" {{subject :subject} :params body :body} (store/register-schema subject body))
           (GET "/:subject/all" [subject] (store/get-all-schemas subject))
           (GET "/:subject/config" [subject] (store/get-config subject))
           (GET "/:subject/latest" [subject] (store/get-latest-schema subject))
           (GET "/:subject/id/:id" [subject id] (store/get-schema subject id))
           (route/not-found "Not Found"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body {:keywords? false})
      (middleware/wrap-json-response)))
