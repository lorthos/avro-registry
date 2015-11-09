(ns avro-registry.store
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [clojure.java.jdbc :as j]
            [avro-registry.env :as e]
            [cheshire.core :refer :all]
            [avro-registry.validator :as val]))

(def spec (:db e/props))

(defn pool
  [spec]
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass (:classname spec))
               (.setJdbcUrl (str "jdbc:" (:subprotocol spec) ":" (:subname spec)))
               (.setUser (:user spec))
               (.setPassword (:password spec))
               (.setMaxIdleTimeExcessConnections (* 30 60))
               (.setMaxIdleTime (* 3 60 60)))]
    {:datasource cpds}))

(def pooled-db (delay (pool spec)))

(defn db [] @pooled-db)

(defn ->table [subject]
  (keyword (str (:table-prefix e/props) subject)))

(defn get-subjects-list []
  (->> (j/query (db) "SELECT * FROM pg_catalog.pg_tables")
       (filter #(.startsWith (:tablename %) (:table-prefix e/props)))
       (map #(:tablename %))
       (map #(subs % 8))))

(defn create-subject [subject]
  (j/db-do-commands (db) (j/create-table-ddl (->table subject)
                                             [:id :smallserial "PRIMARY KEY"]
                                             [:schema "varchar(4096)"])))
(defn get-all-schemas [subject]
  (j/query (db) [(str "SELECT * from " (name (->table subject)))])
  )

(defn get-latest-schema [subject]
  (let [result (j/query (db) [(str "SELECT * from " (name (->table subject)) " ORDER BY id DESC LIMIT 1")])]
    (->> result
         first
         :schema))
  )

(defn get-schema [subject id]
  (let [result (j/query (db) [(str "SELECT * from " (name (->table subject)) " WHERE id = ?") (read-string id)])]
    (->> result
         first
         :schema))
  )

(defn register-schema! [subject body]
  (let [new-schema-string (generate-string body)
        new-schema (val/parse new-schema-string)
        latest-schema-string (get-latest-schema subject)]
    (when latest-schema-string
      (let [latest-schema (val/parse latest-schema-string)]
        (val/validate!! new-schema latest-schema)))
    (j/insert! (db) (->table subject) {:schema new-schema-string})
    )
  )
