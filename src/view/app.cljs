(ns view.app
  (:require [reagent.core]
            [view.css :as css]))

(defonce style-atom (atom {}))

(comment
  @style-atom
  (reset! style-atom {}))

(defn menu-item-component
  [menu-item style args]
  (let [label (str (:name menu-item) " " (:price menu-item) "kr")
        user-id (args :user-id)
        style-hover-key (str label "-" user-id "-hover")
        style-pressed-key (str label "-" user-id "-pressed")]
    [:button {:style (merge css/default-button
                            (css/default-button-hover style style-hover-key)
                            (css/default-button-pressed style style-pressed-key)
                            {:width "100%"})
              :on-click (fn [] nil)
              :on-mouse-down #(swap! style-atom assoc style-pressed-key true)
              :on-mouse-up #(swap! style-atom assoc style-pressed-key false)
              :on-mouse-enter #(swap! style-atom assoc style-hover-key true)
              :on-mouse-leave #(swap! style-atom assoc style-hover-key false)
              }
     label]))

(defn streque-menu-component
  [menu style args]
  [:div {:class "streque-menu"
         :style {:display "flex"
                 :flex-grow 1
                 :flex-direction "column"
                 :padding "4px"}}
   (map (fn [menu-item] (menu-item-component menu-item style args)) menu)])

(defn profile-picture-component
  ; TODO
  [picture]
  [:div {:style {:width "100%"
                 :flex-grow 9
                 :background-color css/accent-color
                 :display "flex"
                 :border-radius css/border-radius
                 :box-shadow "0px 3px 3px 0px rgba(0,0,0,0.5)"}}])

(defn display-name-component
  [display-name]
  [:div {:style {:display "flex"
                 :justify-content "center"
                 :align-items "center"
                 :flex-grow 1}}
   display-name])

(defn user-card-component
  [user menu style]
  (let [element-id (str "user-component-" (:id user))]
    ^{:key element-id}
    [:div {:id element-id
           :style css/default-card}
     [:div {:style {:display "flex"
                    :flex-direction "column"
                    :flex-grow 2
                    :padding "2px"}}
      (profile-picture-component nil)
      (display-name-component (:display-name user))]
     [:div {:style {:margin-left "2px"
                    :display "flex"
                    :flex-grow 1}}
      (streque-menu-component menu style {:user-id (user :id)})]]))

(defn app-component
  [state style]
  (let [users (:users state)
        menu (:menu state)
        generate-user-card-component (fn [user]
                                       (user-card-component user menu style))]
    [:div {:id "app"
           :style {:display "flex"
                   :flex-direction "column"
                   :justify-content "center"
                   :height "100%"
                   :width "100%"}}

     [:header {:style css/general-header}
      [:h1 {:id "header-id"}
       "Streque"]]

     [:div {:id "content"
            :style {:display "flex"
                    :justify-content "center"
                    :width "100%"
                    :height "100%"}}
      [:div {:id "user-cards" :style {:display "flex"
                                      :flex-direction "column"
                                      :justify-content "center"
                                      :width "60%"}}
       (map generate-user-card-component users)]]

     [:footer {:id "footer-menu"
               :style css/general-footer}]]))