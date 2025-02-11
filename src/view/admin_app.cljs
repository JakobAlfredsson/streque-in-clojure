(ns view.admin-app
  (:require [reagent.core]))

(def general-border
  {:border "2px solid black"
   :border-radius "4px"
   :padding "2px"})

(defn menu-item-component
  [menu-item]
  (let [label (str (:name menu-item) " " (:price menu-item) "kr")]
    [:button {:style {:width "100%"
                      :padding "2px"}
              :on-click (fn [] nil)}
     label]))

(defn streque-menu-component
  [menu]
  [:div {:class "menu"
         :style {:display "flex"
                 :flex-grow 1
                 :flex-direction "column"
                 :padding "2px"}}
   (map (fn [menu-item] (menu-item-component menu-item)) menu)])

(defn profile-picture-component
  ;Placeholder
  [picture]
  [:div {:style {:width "100%"
                 :flex-grow 9
                 :background-color "grey"
                 :display "flex"
                 :border-radius "2px"}}])

(defn display-name-component
  [display-name]
  [:div {:style {:display "flex"
                 :justify-content "center"
                 :align-items "center"
                 :flex-grow 1}}
   display-name])

(defn user-card-component
  [user menu]
  (let [element-id (str "user-component-" (:id user))]
    ^{:key element-id}
    [:div {:id element-id
           :style (merge general-border
                         {:margin-top "4px"
                          :height "150px"
                          :width "400px"
                          :display "flex"
                          :flex-direction "row"})}
     [:div {:style {:display "flex"
                    :flex-direction "column"
                    :flex-grow 2
                    :padding "2px"}}
      (profile-picture-component nil)
      (display-name-component (:display-name user))]
     [:div {:style {:margin-left "2px"
                    :display "flex"
                    :flex-grow 1}}
      (streque-menu-component menu)]]))

(defn app-component
  [state]
  [:h1 "Streque Admin"])