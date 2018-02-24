(ns app.dropbox
  (:require [versioned.logger :as logger]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [versioned.components.config :refer [get-env]]))

(def base-url "https://content.dropboxapi.com")

(def files-per-folder 100)

(def upload-url (str base-url "/2/files/upload"))

(defn api-token [app]
  (get-in app [:config :dropbox-api-token]))

(defn enabled? [app]
  (boolean (api-token app)))

(defn file-path [app doc]
  (let [id-folder (quot (:id doc) files-per-folder)]
    (str "/" (get-env (:config app)) "/" (:type doc) "/" id-folder "/" (:id doc) ".json")))

(defn str-to-byte-array [value]
  (byte-array (map byte value)))

; curl -X POST https://content.dropboxapi.com/2/files/upload \
;     --header "Authorization: $AUTH" \
;     --header "Dropbox-API-Arg: {\"path\": \"/README.md\",\"mode\": \"add\",\"autorename\": true,\"mute\": false}" \
;     --header "Content-Type: application/octet-stream" \
;     --data-binary README.md
(defn save [app doc]
  (if (enabled? app)
    (let [path (file-path app doc)
          headers {
            :Authorization (str "Bearer " (api-token app))
            :Dropbox-API-Arg (json/generate-string {:path path :mode "add" :autorename true :mute false})
            :Content-Type "application/octet-stream"
          }
          body (str-to-byte-array (json/generate-string doc))
          result (client/post upload-url {:headers headers
                                          :body body
                                          :as :json
                                          :debug true
                                          :throw-exceptions false})]
        result)))
