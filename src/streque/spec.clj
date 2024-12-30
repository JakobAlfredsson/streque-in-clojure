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
                               ::last-name]
                      :opt-un [::display-name
                               ::balance]))

; http-User
(s/def ::http-user ::user) ; Place holder until http-user and user diverge in terms of requirements