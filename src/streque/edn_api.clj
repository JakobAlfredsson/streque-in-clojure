(ns streque.edn-api
  (:require [streque.core :as core]))

(defonce db-atom (atom nil))

(defn get-all-users
  []
  (let [db (deref db-atom)]
    (core/get-all-users db)))

(defn get-menu
  []
  (let [db (deref db-atom)]
    (core/get-menu db)))