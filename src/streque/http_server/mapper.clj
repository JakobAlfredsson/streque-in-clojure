(ns streque.http-server.mapper
  (:require [clojure.test :refer [is deftest]]
            [clojure.spec.alpha :as s]
            ;[clojure.spec.gen.alpha :as gen]
            [streque.spec]
            [clojure.spec.test.alpha :as stest]))


(deftest generative-tests
  (is (= true
         (-> (stest/enumerate-namespace 'streque.http-server.mapper)
             stest/check
             first
             :clojure.spec.test.check/ret 
             :result))))

(defn assoc-if-not-nil
  [map key val]
  (if-not (nil? val)
    (assoc map key val)
    map))

(defn user->http-user
  [user]
  {:pre [(s/valid? :streque.spec/user user)]
   :post [(s/valid? :streque.spec/http-user %)]}
  (let [required-fields {:id (:id user)}]
    (-> required-fields
        (assoc-if-not-nil :first-name (:first-name user))
        (assoc-if-not-nil :last-name (:last-name user))
        (assoc-if-not-nil :display-name (:display-name user))
        (assoc-if-not-nil :balance (:balance user)))))

(s/fdef user->http-user
  :args (s/cat :user :streque.spec/user)
  :ret :streque.spec/user
  :fn #(= (:ret %) (-> % :args :user)))

(defn menu->http-menu
  [menu]
  {:pre [(s/valid? :streque.spec/menu menu)]
   :post [(s/valid? :streque.spec/http-menu %)]}
  (let [get-required-fields (fn [menu-item] {:id (:id menu-item)
                                             :name (:name menu-item)
                                             :price (:price menu-item)})]
    (map get-required-fields menu)))

(s/fdef menu->http-menu
  :args (s/cat :menu :streque.spec/menu)
  :ret :streque.spec/menu
  :fn #(= (:ret %) (-> % :args :menu)))



(comment
  (stest/summarize-results (stest/check `menu->http-menu))
  (stest/check) ; This makes my computer unusable for some reason
  )