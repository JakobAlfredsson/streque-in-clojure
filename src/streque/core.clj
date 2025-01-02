(ns streque.core
  (:require [clojure.test :refer [is]]
            [datomic.client.api :as d]
            [streque.schemas :as schemas]
            ;[dev-db]
            [streque.unit-test-db :refer [unit-test-connection
                                          unit-test-db]]
            [streque.db-mapper :refer [db-user->user]]))

(defn get-all-users
  "Gets all entities with a :user/id from the database."
  {:test (fn []
           (is (= 2
                  (-> (d/with unit-test-db
                              {:tx-data [[:db/add 1 :user/id "u1"]
                                         [:db/add 2 :user/id "u2"]]})
                      (:db-after)
                      (get-all-users)
                      (count)))
               "Finds all users")
           (is (= []
                  (-> (d/with unit-test-db
                              {:tx-data []})
                      (:db-after)
                      (get-all-users)))
               "Returns an empty list if there are no users"))}
  [db]
  (let [get-all-users-q '[:find (pull ?user [*])
                          :where [?user :user/id _]]]
    (->> (d/q get-all-users-q db)
         (flatten)
         (map db-user->user))))

; TODO
(defn add-user!
  "Adds the user as specified by the user-map to the database that is connected via conn."
  [connection user-map]
  (d/transact connection {:tx-data [user-map]}))

; TODO
(defn get-user
  "Gets the entity where (= user-id (:user/id entity)) from the database."
  {:test (fn []
           (is (= 1
                  (-> (d/with unit-test-db
                              {:tx-data [[:db/add 1 :user/id "u1"]
                                         [:db/add 2 :user/id "u2"]]})
                      (:db-after)
                      (get-user "u1")))
               "Gets the correct user if it exists.")
           (is (= nil
                  (-> (d/with unit-test-db {:tx-data [[:db/add 1 :user/id "u1"]]})
                      (:db-after)
                      (get-user "u2")))
               "Returns nil if the user doesn't exist.")
           (is (= #:user{:first-name "Test"
                         :last-name "Testsson"}
                  (as-> unit-test-db $
                    (d/with $ {:tx-data [[:db/add 1 :user/id "u1"]
                                         [:db/add 1 :user/first-name "Test"]
                                         [:db/add 1 :user/last-name "Testsson"]]})
                    (:db-after $)
                    (d/pull $ '[:user/first-name :user/last-name] (get-user $ "u1"))))
               "Can retrieve attributes of the user."))}
  [db user-id]
  (let [get-user-q '[:find ?e
                     :in $ ?user-id
                     :where [?e :user/id ?user-id]]]
    (-> (d/q get-user-q db user-id)
        (ffirst))))

; TODO
(defn add-quote!
  "Adds the quote as specified by the quote-map to the database that is connected via conn."
  [connection uploading-user-id quote-map]
  (let [db (d/db connection)
        uploading-user (get-user db uploading-user-id)
        quote-map (merge quote-map {:quote/uploaded-by uploading-user})]
    (d/transact connection {:tx-data [quote-map]})))

; TODO
(defn get-quote
  "Gets the quote with the given quote-id from the database."
  [db quote-id]
  (let [get-quote-q '[:find ?e
                      :in $ ?quote-id
                      :where [?e :quote/id ?quote-id]]]
    (-> (d/q get-quote-q db quote-id)
        (ffirst))))

; Probably not usable
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

; Probably not usable
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

(comment
  ; https://docs.datomic.com/client-tutorial/client.html
  ; https://docs.datomic.com/client-api/datomic.client.api.html
  ; https://docs.datomic.com/clojure/index.html#datomic.api/entity
  ; https://stackoverflow.com/questions/14189647/get-all-fields-from-a-datomic-entity

  ; The references to the connection and the db are likely wrong.
  ;; (d/transact dev-db/connection {:tx-data schemas/user-schema})
  ;; (add-user! dev-db/connection {:user/id "u3"
  ;;                                     :user/display-name "Test"})
  ;; (get-user dev-db/db "u1")
  ;; (d/pull dev-db/db '[*] (get-user dev-db/db "u1"))

  ;; (d/transact dev-db/connection {:tx-data schemas/quote-schema})
  ;; (add-quote! dev-db/connection "u1" {:quote/id "q3" :quote/quote "Test-quote"})
  ;; (get-quote dev-db/db "q3")
  ;; (d/pull dev-db/db '[*] (get-quote dev-db/db "q3"))
  ;; (d/pull dev-db/db '[:quote/_uploaded-by] (get-user dev-db/db "u1"))
  ;; (d/pull dev-db/db '[* {[:quote/_uploaded-by] [:quote/id :quote/quote]}] (get-user dev-db/db "u1"))
  ;; (:db/id (:quote/uploaded-by (d/pull dev-db/db '[:quote/uploaded-by] (get-quote dev-db/db "q3"))))
  ;; (d/pull dev-db/db '[*] (:db/id (:quote/uploaded-by (d/pull dev-db/db '[:quote/uploaded-by] (get-quote dev-db/db "q3")))))

  ;; (d/datoms dev-db/db {:index :eavt})

  ;; (d/delete-database dev-db/client {:db-name "streque"}))
  )