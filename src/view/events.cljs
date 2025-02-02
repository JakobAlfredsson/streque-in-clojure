(ns view.events
  (:require [view.state :as state]
            [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]))

;; (defn js-arr->vector
;;   [js-arr]
;;   (reduce conj [] js-arr))

;; (defn jsobj->map
;;   [js-obj]
;;   ; Object.entries(obj)
;;   (js/Object.entries js-obj))

;; (defn jsx->clj
;;   [x]
;;   (into {} (for [k (.keys js/Object x)] [k (aget x k)])))

;; (defn stream->clj
;;   [readable-stream]
;;   (-> readable-stream
;;       (.json)
;;       (js->clj)))

;; (defn get-all-users!
;;   [{}]
;;   (let [url "http://localhost:9499/get-all-users"
;;         replace-users (fn [state new-users]
;;                         (assoc state :users new-users))]
;;     (-> (js/fetch url)
;;         (.then (fn [response]
;;                  (as-> response $
;;                    ;(.json $)
;;                    ;(js->clj $)
;;                    ;(deref $)
;;                    (swap! state/state-atom assoc :users $)
;;                    (js/console.log))))
;;         (.catch #(js/console.log %))
;;         (.finally #(js/console.log "cleanup")))))

(defn get-all-users!
  [& _]
  (go
    (let [url "http://localhost:9499/get-all-users"
          users (<p! (js/fetch url))
          users (<p! (.json users))
          ;users (<p! (.parse js/JSON users))
          ;users (<p! (.stringify js/JSON users))
          ;users (js->clj (-> users js/JSON.stringify js/JSON.parse))
          users (js->clj users {:keywordize-keys true})
          ;users (<p! (js-arr->vector users))
          ]
      (try 
        (<p! (swap! state/state-atom assoc :users users))
        (catch js/Error err (js/console.log (ex-cause err)))))))

; https://www.martinklepsch.org/posts/working-with-promises-in-clojurescript-repls
; https://gist.github.com/pesterhazy/c4bab748214d2d59883e05339ce22a0f
; https://stackoverflow.com/questions/32467299/clojurescript-convert-arbitrary-javascript-object-to-clojure-script-map

(comment
  (get-all-users! {})
  (println @state/state-atom)
  (js->clj @state/state-atom {:keywordize-keys true})
  ;(jsobj->map (first (:users @state/state-atom)))
  )