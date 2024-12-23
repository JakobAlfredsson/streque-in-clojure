(ns streque.view.app
  (:require [reagent.core]
            [streque.view.events :as events]))

(defn user-component
  [user]
  (let [element-id (str "user-component-" (:id user))]
    ^{:key element-id}
    [:div {:id element-id
           :style {:border "1px solid black"
                   :margin-top "5px"
                   :padding "5px"}}
     [:h2 (:display-name user)]
     [:h3 (str (:first-name user) " " (:last-name user))]]))

(defn app-component
  [state]
  [:div
   [:h1 {:id "header-id"} 
    "Test"]
   [:button {:on-click (fn [] (events/get-all-users! {}))}
    "get-all-users"]
   [:div {:id "users" :style {:width "50%"}}
    (map user-component (:users state))]])