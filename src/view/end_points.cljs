(ns view.end-points
  (:require [view.state :as state]
            [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]))

(def backend-url
  "http://localhost:9499")

(def end-points
  {:get-users "/get-all-users"
   :streque-menu "/get-streque-menu"})

(defn get-all-users!
  [& _]
  (go
    (let [full-url (str backend-url (:get-users end-points))
          users (<p! (js/fetch full-url))
          users (<p! (.json users))
          users (js->clj users {:keywordize-keys true})]
      (try
        (<p! (swap! state/state-atom assoc :users users))
        (catch js/Error err (js/console.log (ex-cause err)))))))

(defn get-streque-menu!
  []
  (go
    (let [full-url (str backend-url (:streque-menu end-points))
          streque-menu (<p! (js/fetch full-url))
          streque-menu (<p! (.json streque-menu))
          streque-menu (js->clj streque-menu {:keywordize-keys true})]
      (try
        (<p! (swap! state/state-atom assoc :menu streque-menu))
        (catch js/Error err (js/console.log (ex-cause err)))))))

(defn get-public-state!
  []
  (get-streque-menu!)
  (get-all-users!))