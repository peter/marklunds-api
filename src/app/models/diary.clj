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
          :properties {
            :body {:type "string"}
          }
          :additionalProperties false
          :required [:title]
        }
      }
  ))
