(defproject versioned-example "0.1.0-SNAPSHOT"
  :description "Clojure CMS REST API based on MongoDB for www.marklunds.com"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [versioned "0.11.24"]]
  :min-lein-version "2.0.0"
  :uberjar-name "marklunds-standalone.jar"
  :main ^:skip-aot app.core
  :target-path "target/%s"
  :profiles {
    :uberjar {:aot :all}
    :dev {:dependencies [[midje "1.6.3"]]}})
