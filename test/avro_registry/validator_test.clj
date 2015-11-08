(ns avro-registry.validator-test
  (:require [clojure.test :refer :all]
            [avro-registry.validator :as val]
            [clojure.java.io :as io]))

(def schema1 (slurp (io/resource "schema1.json")))
(def schema2-bad (slurp (io/resource "schema2-bad.json")))
(def schema2-good (slurp (io/resource "schema2-good.json")))

(deftest test-validator
  (testing "backward compatibility"
    (is (nil? (val/validate!! {} schema2-good schema1)))
    (is (nil? (val/validate!! {} schema2-bad schema1)))
    ))
