(ns streque.edn-api
  (:require [streque.core :as core]
            [streque.db :as db]))

(defonce db-atom (atom db/local-dev-db))

(defn get-all-users
  []
  (let [db (deref db-atom)]
    (core/get-all-users db)))