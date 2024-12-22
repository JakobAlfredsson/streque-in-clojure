(ns streque.db
  (:require [datomic.client.api :as d]
            [clojure.walk]
            [clojure.test :refer [is]]))

(def user-schema [{:db/ident :user/id
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/unique      :db.unique/identity
                   :db/doc "The id of the user"}

                  {:db/ident :user/first-name
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc "The first name of the user"}

                  {:db/ident :user/last-name
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc "The last name of the user"}

                  {:db/ident :user/display-name
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc "The display name of the user"}])

(def quote-schema [{:db/ident :quote/id
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/unique      :db.unique/identity
                    :db/doc "The id of the quote"}

                   {:db/ident :quote/quote
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "The actual quote"}

                   {:db/ident :quote/said-by
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "Who the quote is attributed to"}

                   {:db/ident :quote/uploaded-by
                    :db/valueType :db.type/ref
                    :db/cardinality :db.cardinality/one
                    :db/doc "The user who uploaded the quote"}])

; --------------------------------------------------
;            Start "production" database
; --------------------------------------------------
(def client (d/client {:server-type :datomic-local
                       :storage-dir :mem
                       :system "ci"}))
(d/create-database client {:db-name "streque"})
(def connection (d/connect client {:db-name "streque"}))
(def db (d/db connection))
; --------------------------------------------------

; --------------------------------------------------
;               Start local-test-db
; --------------------------------------------------
(def local-test-client (d/client
                        {:server-type :datomic-local
                         :storage-dir :mem
                         :system "ci"}))
(def local-test-db-arg-map {:db-name "local-test-db"})
(d/create-database local-test-client local-test-db-arg-map)
(def local-test-connection (d/connect local-test-client local-test-db-arg-map))
(def local-test-db (d/with-db local-test-connection))
(d/transact local-test-connection {:tx-data user-schema})
(d/transact local-test-connection {:tx-data quote-schema})
; --------------------------------------------------

(defn add-user!
  "Adds the user as specified by the user-map to the database that is connected via conn."
  [connection user-map]
  (d/transact connection {:tx-data [user-map]}))

(defn get-user
  "Gets the entity where (= user-id (:user/id entity) from the database."
  {:test (fn []
           (is (= 1
                  (-> (d/with local-test-db
                              {:tx-data [[:db/add 1 :user/id "u1"]
                                         [:db/add 2 :user/id "u2"]]})
                      (:db-after)
                      (get-user "u1")))
               "Gets the correct user if it exists.")
           (is (= nil
                  (-> (d/with local-test-db {:tx-data [[:db/add 1 :user/id "u1"]]})
                      (:db-after)
                      (get-user "u2")))
               "Returns nil if the user doesn't exist.")
           (is (= #:user{:first-name "Test"
                         :last-name "Testsson"}
                  (as-> local-test-db $
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

(defn get-all-users
  "Gets all entities with a :user/id from the database."
  [db]
  (let [get-all-users-q '[:find (pull ?user [*])
                          :where [?user :user/id _]]]
    (->> (d/q get-all-users-q db)
         (flatten))))

(defn add-quote!
  "Adds the quote as specified by the quote-map to the database that is connected via conn."
  [connection uploading-user-id quote-map]
  (let [db (d/db connection)
        uploading-user (get-user db uploading-user-id)
        quote-map (merge quote-map {:quote/uploaded-by uploading-user})]
    (d/transact connection {:tx-data [quote-map]})))

(defn get-quote
  "Gets the quote with the given quote-id from the database."
  [db quote-id]
  (let [get-quote-q '[:find ?e
                      :in $ ?quote-id
                      :where [?e :quote/id ?quote-id]]]
    (-> (d/q get-quote-q db quote-id)
        (ffirst))))

(comment
  ; https://docs.datomic.com/client-tutorial/client.html
  ; https://docs.datomic.com/client-api/datomic.client.api.html
  ; https://docs.datomic.com/clojure/index.html#datomic.api/entity
  ; https://stackoverflow.com/questions/14189647/get-all-fields-from-a-datomic-entity

  (d/transact connection {:tx-data user-schema})
  (add-user! connection {:user/id "u3"
                         :user/display-name "Test"})
  (get-user db "u1")
  (d/pull db '[*] (get-user db "u1"))

  (d/transact connection {:tx-data quote-schema})
  (add-quote! connection "u1" {:quote/id "q3" :quote/quote "Test-quote"})
  (get-quote db "q3")
  (d/pull db '[*] (get-quote db "q3"))
  (d/pull db '[:quote/_uploaded-by] (get-user db "u1"))
  (d/pull db '[* {[:quote/_uploaded-by] [:quote/id :quote/quote]}] (get-user db "u1"))
  (:db/id (:quote/uploaded-by (d/pull db '[:quote/uploaded-by] (get-quote db "q3"))))
  (d/pull db '[*] (:db/id (:quote/uploaded-by (d/pull db '[:quote/uploaded-by] (get-quote db "q3")))))

  (d/datoms db {:index :eavt})

  (d/delete-database client {:db-name "streque"}))