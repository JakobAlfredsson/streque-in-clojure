(ns streque.view.app
  (:require [reagent.core]))

(defn app-component
  [db]
  [:div
   [:h1 {:id "header-id"}
    "Test"]])