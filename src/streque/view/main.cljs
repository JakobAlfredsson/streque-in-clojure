(ns ^:figwheel-hooks streque.view.main
  (:require [reagent.dom :as reagent-dom]
            [streque.view.dom :as dom]
            [streque.view.app :refer [app-component]]
            [streque.view.state :as state]))



(defn render!
  [] ;
  (reagent-dom/render (app-component (deref state/state-atom))
                      (dom/get-app-element)))


;; (when-not (deref db/db-atom)
;;   (js/addEventListener "resize"
;;                        (fn []
;;                          (swap! db/db-atom
;;                                 assoc
;;                                 :screen
;;                                 (dom/get-screen-size))))

 (add-watch state/state-atom :watcher (fn [_ _ _ _] (render!)))

;;   (reset! db/db-atom {:states (list (core/create-state ["##"
;;                                                         "###"
;;                                                         "# ###"]))
;;                       :redo-states (list)
;;                       :screen (dom/get-screen-size)}))

(defn on-js-reload
  {:after-load true}
  []
  (render!))
