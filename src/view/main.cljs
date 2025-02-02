(ns ^:figwheel-hooks view.main
  (:require [reagent.dom :as reagent-dom]
            [view.dom :as dom]
            [view.app :refer [app-component]]
            [view.state :as state]
            [view.events :as events]
            [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]))



(defn get-menu!
  []
  (go
    (let [url "http://localhost:9499/get-menu"
          menu (<p! (js/fetch url))
          menu (<p! (.json menu))
          menu (js->clj menu {:keywordize-keys true})]
      (try
        (<p! (swap! state/state-atom assoc :menu menu))
        (catch js/Error err (js/console.log (ex-cause err)))))))



(defn get-public-state!
  []
  (get-menu!)
  (events/get-all-users!))


(defn render
  []
  (reagent-dom/render (app-component (deref state/state-atom))
                      (dom/get-app-element)))

(defonce start-up 
  (do 
    (get-public-state!)
    (render) 
    true))

(add-watch state/state-atom :watcher (fn [_ _ _ _] (render)))

(defn ^:after-load re-render
  []
  (render))