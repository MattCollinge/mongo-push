(defproject mongo-push "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
  					[com.novemberain/monger "2.0.0"]
  					[cheshire "5.4.0"]
  					[clj-time "0.6.0"]]
  :main ^:skip-aot mongo-push.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
