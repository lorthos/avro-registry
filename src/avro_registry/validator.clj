(ns avro-registry.validator
  (:require [clojure.tools.logging :as log])
  (:import (org.apache.avro SchemaValidator SchemaValidatorBuilder)))

(def validators {:no-op    (proxy [SchemaValidator] []
                             (validate [_ _]
                               (log/infof "Nothing to do in no-op validator")))
                 :backward (doto
                             (SchemaValidatorBuilder.)
                             (.canReadStrategy)
                             (.validateLatest))

                 :forward  (doto
                             (SchemaValidatorBuilder.)
                             (.canBeReadStrategy)
                             (.validateLatest))

                 :full     (doto
                             (SchemaValidatorBuilder.)
                             (.mutualReadStrategy)
                             (.validateLatest))
                 }

  )

(def default-validator-config {:validator :full})

(defn validate!! [config new-schema old-schema]
  (let [^SchemaValidator validator ((:validator (merge default-validator-config config)) validators)
        old-schema-as-list (doto (java.util.ArrayList.) (.add old-schema))]
    (.validate validator new-schema old-schema-as-list)
    )
  )
