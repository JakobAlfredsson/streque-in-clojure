(ns ^:figwheel-hooks view.main
  (:require [reagent.dom :as reagent-dom]
            [view.dom :as dom]
            [view.app :refer [app-component]]
            [view.state :as state]
            [view.end-points :as end-points]))

(defn render
  []
  (reagent-dom/render (app-component (deref state/state-atom))
                      (dom/get-app-element)))

(defn ^:after-load re-render
  []
  (render))

(defonce start-up
  (do
    (end-points/get-public-state!)
    (render)
    (add-watch state/state-atom :watcher (fn [_ _ _ _] (render)))
    true))