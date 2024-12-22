(ns streque.db-mapper)

(defn db-user->user
  [db-user]
  {:id (:user/id db-user)
   :first-name (:user/first-name db-user)
   :last-name (:user/last-name db-user)
   :display-name (:user/display-name db-user)
   :balance (:user/balance db-user)})