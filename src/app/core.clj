(ns app.core
  (:require [versioned :as versioned]
            [versioned.components.config :refer [get-env]]))

(def default-config {
  :models {
    :blog-posts "app.models.blog-posts/spec"
  }
  :mongodb-url "mongodb://127.0.0.1/marklunds"
  :search-enabled true
  :algoliasearch-index-name (str "marklunds-" (get-env {}))
  :algoliasearch-application-id nil ; provided with env variable
  :algoliasearch-api-key nil ; provided with env variable
  :dropbox-api-token nil ; provided with env variable
})

(defn -main [& {:as custom-config}]
  (let [config (merge default-config custom-config)
        args-list (apply concat (seq config))]
    (apply versioned/-main args-list)))
