(ns ^:figwheel-hooks streque.view.main
  (:require [reagent.dom :as reagent-dom]
            [streque.view.dom :as dom]
            [streque.view.app :refer [app-component]]
            [streque.view.state :as state]
            [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]))



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

(defn on-js-reload
  {:after-load true}
  []
  (get-menu!)
  (render!))
