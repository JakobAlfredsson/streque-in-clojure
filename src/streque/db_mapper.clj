(ns streque.db-mapper)

(defn db-user->user
  [db-user]
  {:id (:user/id db-user)
   :first-name (:user/first-name db-user)
   :last-name (:user/last-name db-user)
   :display-name (:user/display-name db-user)
   :balance (:user/balance db-user)})

(defn db-menu-item->menu-item
  [db-menu-item]
  {:id (:menu-item/id db-menu-item)
   :name (:menu-item/name db-menu-item)
   :price (:menu-item/price db-menu-item)})