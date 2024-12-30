(ns streque.spec
  (:require [clojure.spec.alpha :as s]))

; General specs
(s/def ::id string?)

; User
(s/def ::first-name string?)
(s/def ::last-name string?)
(s/def ::display-name string?)
(s/def ::balance number?)

(s/def ::user (s/keys :req-un [::id
                               ::first-name
                               ::last-name
                               ::balance]
                      :opt-un [::display-name]))

; http-User
(s/def ::http-user (s/keys :req-un [::id]
                           :opt-un [::first-name
                                    ::last-name
                                    ::display-name
                                    ::balance]))