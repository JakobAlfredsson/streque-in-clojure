(ns streque.http-server.mapper
  (:require [clojure.spec.alpha :as s]))

(defn user->http-user 
  [user]
  {:pre [(s/valid? ::user user)]
   :post [(s/valid? ::http-user %)]}
  {:id (:id user)
   :first-name (:first-name user)
   :last-name (:last-name user)
   :display-name (:display-name user)
   :balance (:balance user)})