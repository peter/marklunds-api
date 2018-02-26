(ns app.models.diary
  (:require [versioned.model-spec :refer [generate-spec]]
            [versioned.model-includes.content-base-model :refer [content-base-spec]]
            [app.models.dropbox-model :refer [dropbox-spec]]))

(def model-type :diary)

(defn spec [config]
  (generate-spec
      (content-base-spec model-type)
      (dropbox-spec)
      {
        :type model-type
        :schema {
          :type "object"
          :x-meta {
            :admin_properties [:body]
          }
          :properties {
            :body {:type "string"}
            :legacy_created_at {:type "string" :format "date-time" :x-meta {:versioned false}}
          }
          :additionalProperties false
          :required [:body]
        }
      }
  ))
