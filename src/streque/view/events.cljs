(ns streque.view.events
  (:require [streque.view.main :as main]))

(defn stream->clj
  [readable-stream]
  (-> readable-stream
      (.json)
      (js->clj)))

(defn get-all-users!
  [{}]
  (let [url "http://localhost:9499/get-all-users"
        replace-users (fn [state new-users]
                        (assoc state :users new-users))]
    (-> (js/fetch url)
        (.then (fn [response]
                 (as-> response $
                   (stream->clj $))))
        (.then #(swap! main/state-atom assoc :users %))
        (.catch #(js/console.log %))
        (.finally #(js/console.log "cleanup")))))

; https://www.martinklepsch.org/posts/working-with-promises-in-clojurescript-repls
; https://gist.github.com/pesterhazy/c4bab748214d2d59883e05339ce22a0f

(comment
  (get-all-users! {})
  @main/state-atom)