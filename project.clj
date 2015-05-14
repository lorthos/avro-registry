(defproject avro-registry "0.1.0-SNAPSHOT"
            :description "FIXME: write description"
            :url "http://example.com/FIXME"
            :min-lein-version "2.0.0"
            :dependencies [[org.clojure/clojure "1.6.0"]
                           [org.clojure/tools.logging "0.3.1"]
                           [compojure "1.3.1"]
                           [ring/ring-defaults "0.1.2"]
                           [ring/ring-json "0.1.2"]
                           [com.mchange/c3p0 "0.9.2.1"]
                           [org.clojure/java.jdbc "0.3.6"]
                           [postgresql/postgresql "8.4-702.jdbc4"]
                           [cheshire "5.4.0"]
                           ]
            :plugins [[lein-ring "0.8.13"]]
            :ring {:handler avro-registry.handler/app}
            :profiles
            {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring-mock "0.1.5"]]}})
