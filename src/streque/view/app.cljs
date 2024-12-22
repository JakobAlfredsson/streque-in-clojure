(ns streque.view.app
  (:require [reagent.core]
            [streque.view.events :as events]))



(defn app-component
  [db]
  [:div
   [:h1 {:id "header-id"} 
    "Test"]
   [:button {:on-click (fn [] (events/get-all-users! {}))}
    "get-all-users"]])