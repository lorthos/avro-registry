(ns avro-registry.store
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [clojure.java.jdbc :as j]
            [avro-registry.env :as e]
            [cheshire.core :refer :all]))

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
  (keyword (str "subject_" subject)))

(defn get-subjects-list []
  (filter #(.startsWith (:tablename %) "subject_") (j/query (db) "SELECT * FROM pg_catalog.pg_tables"))
  )

(defn create-subject [subject]
  (j/db-do-commands (db) (j/create-table-ddl (->table subject)
                                             [:id :smallserial "PRIMARY KEY"]
                                             [:schema "varchar(4096)"]))
  )

(defn register-schema [subject body]
  (j/insert! (db) (->table subject) {:schema (generate-string body)})
  )

(defn get-all-schemas [subject]
  (j/query (db) [(str "SELECT * from " (name (->table subject)))])
  )

(defn get-config [subject]
  ;TODO
  (println subject)
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
