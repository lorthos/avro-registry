(ns avro-registry.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [avro-registry.handler :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/ping"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Pong"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
