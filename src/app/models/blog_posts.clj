(ns app.models.blog-posts
  (:require [versioned.model-spec :refer [generate-spec]]
            [versioned.base-models.published :as base-model]
            [app.models.dropbox-model :refer [dropbox-spec]]))

(def model-type :blog_posts)

(defn spec [config]
  (generate-spec
      (base-model/spec model-type)
      (dropbox-spec)
      {
        :type model-type
        :schema {
          :type "object"
          ; :x-meta {
          ;   :admin_properties [:subject :body :created_at :version :published_version]
          ; }
          :properties {
            :subject {:type "string"}
            :body {:type "string" :x-meta {:form_field "textarea"}}
            :comments_count {:type "integer"}
            :legacy_created_at {:type "string" :format "date-time" :x-meta {:versioned false}}
          }
          :additionalProperties false
          :required [:subject :body]
        }
      }
  ))
