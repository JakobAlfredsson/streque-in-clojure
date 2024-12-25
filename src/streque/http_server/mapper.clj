(ns streque.http-server.mapper)

(defn user->http-user
  [user]
  {:id (:id user)
   :first-name (:first-name user)
   :last-name (:last-name user)
   :display-name (:display-name user)
   :balance (:balance user)})