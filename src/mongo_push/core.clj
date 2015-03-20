(ns mongo-push.core
  (:gen-class)
  (:require [clojure.java.io :as io]
  			[clojure.repl :refer [set-break-handler!]]
  			[cheshire.core :as json]
  			[clj-time.core :as t]
  			[clj-time.coerce :as c]
  			[cheshire.generate :refer [add-encoder encode-str encode-date remove-encoder]]
  			[monger.core :as mg]
  			[monger.collection :refer [insert insert-batch find-maps find-one-as-map find-map-by-id]]
  			[monger.joda-time])
  	(:import [com.mongodb MongoOptions ServerAddress])
  	(:import com.mongodb.WriteConcern)
  	(:import [org.bson.types ObjectId]))


(defn connect-to-mongo[host port]
	(let [	conn (mg/connect { :host host :port port })
			db (mg/get-db conn "mongo-push")]
			db
	))


(defn run-session[db payload]
	(insert db "testdata" payload ))

(defn get-merged-doc[template]
	(let [	oid (ObjectId.)
			epoch {:epochTime (c/to-long (t/now))}]
		(merge template {:_id oid :epochTime epoch})))

(defn -main
  "I don't do a whole lot ... yet."
  [& *command-line-args*]
  (let [number-inserts (Long/valueOf (first *command-line-args*))
  		host (nth *command-line-args* 1 "localhost")
  		port (Long/valueOf (nth *command-line-args* 2 27017))
  		_ (mg/set-default-write-concern! WriteConcern/FSYNC_SAFE)
  		_ (println "Connecting to:" host ", port:" port "to write" number-inserts "docs...")
  		db (connect-to-mongo host port)
  		template (json/parse-stream (clojure.java.io/reader (io/resource "input.json")) true)
  		docs-to-insert (doall (map (fn[iteration] (get-merged-doc template)) (range number-inserts)))]

  			(time (doall (map (fn[payload](run-session db payload)) docs-to-insert)))
  			(shutdown-agents)
  			; (doall (map (fn [iteration](run-session db {:iamatest "blaa" :iteration iteration})) (range number-inserts)))
  		))
