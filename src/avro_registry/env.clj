(ns avro-registry.env
  "Application properties"
  (:import (java.io PushbackReader File))
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]))

(defn load-props [filename]
  (with-open [r (io/reader filename)]
    (binding [*read-eval* false]
      (read (PushbackReader. r)))))

(defn log-exec [f]
  (fn [& args]
    (let [res (apply f args)]
      (log/debug (str "Function: " f " Args: " args " Result: " res))
      res)))

(defn make-config-path []
  (let [config (or (System/getProperty "app.config.path")
                   (let [path (str (System/getProperty "user.home") "/.registry.edn")]
                     (if (.exists (File. path))
                       path
                       nil))
                   "./resources/config.edn")
        ]
    (log/warnf "Chosed %s as configuration path!" config)
    config))

(def ^:dynamic props ((log-exec load-props) (-> (make-config-path) io/file)))