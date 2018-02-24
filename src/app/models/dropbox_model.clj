(ns app.models.dropbox-model
  (:require [versioned.logger :as logger]
            [app.dropbox :as dropbox]))

(defn save-callback [doc options]
  (logger/debug (:app options) "dropbox-model/save-callback " (:type doc) " " (:id doc))
  (let [result (dropbox/save (:app options) doc)]
    (logger/debug (:app options) "dropbox-model/save-callback result=" result)
    doc))

(def dropbox-callbacks {
  :save {
    :after [save-callback]
  }
})

(defn dropbox-spec [] {
  :callbacks dropbox-callbacks
})
