(ns app.models.blog-posts
  (:require [versioned.model-spec :refer [generate-spec]]
            [versioned.model-includes.content-base-model :refer [content-base-spec]]
            [app.models.dropbox-model :refer [dropbox-spec]]))

(def model-type :blog_posts)

(defn spec [config]
  (generate-spec
      (content-base-spec model-type)
      (dropbox-spec)
      {
        :type model-type
        :schema {
          :type "object"
          :properties {
            :subject {:type "string"}
            :body {:type "string"}
            :comments_count {:type "integer"}
            :legacy_created_at {:type "string" :format "date-time" :x-meta {:versioned false}}
          }
          :additionalProperties false
          :required [:subject :body]
        }
      }
  ))
