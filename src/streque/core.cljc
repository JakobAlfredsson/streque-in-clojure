(ns streque.core
  (:require [clojure.test :refer [is]]))

(defn create-user
  {:test (fn []
           (is (= {:id "u0"
                   :name "Default"
                   :balance 100}
                  (create-user))
               "Default user")
           (is (= (as-> (create-user {:id "u1" :name "Test"}) $
                    {:id (:id $) :name (:name $)})
                  {:id "u1" :name "Test"})
               "Overriding :id and :name"))}
  ([]
   {:id "u0"
    :name "Default"
    :balance 100})
  ([args]
   (merge (create-user) args)))

(defn create-state
  {:test (fn []
           (is (= {:users #{(create-user {:id "u0"})
                            (create-user {:id "u1"})}}
                  (create-state))
               "Default state is to have two default users")
           (is (= #{"u2"}
                  (->> (create-state {:users #{(create-user {:id "u2"})}})
                       (:users)
                       (map :id)
                       (set)))
               "Overriding :users"))}
  ([]
   {:users #{(create-user {:id "u0"})
             (create-user {:id "u1"})}})
  ([args]
   (merge (create-state) args)))