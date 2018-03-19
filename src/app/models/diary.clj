(ns app.models.diary
  (:require [versioned.model-spec :refer [generate-spec]]
            [versioned.base-models.published :as base-model]
            [app.models.dropbox-model :refer [dropbox-spec]]))

(def model-type :diary)

(defn spec [config]
  (generate-spec
      (base-model/spec model-type)
      (dropbox-spec)
      {
        :type model-type
        :schema {
          :type "object"
          :x-meta {
            :admin_properties [:body :created_at]
            :require-read-auth true
          }
          :properties {
            :body {:type "string" :x-meta {:form_field "textarea"}}
            :legacy_created_at {:type "string" :format "date-time" :x-meta {:versioned false}}
          }
          :additionalProperties false
          :required [:body]
        }
      }
  ))
