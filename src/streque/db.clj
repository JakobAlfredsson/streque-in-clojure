(ns streque.db
  (:require [datomic.client.api :as d]
            [clojure.walk]))

; (require '[datomic.client.api :as d])
(def client (d/client {:server-type :datomic-local
                       :storage-dir :mem
                       :system "ci"}))

(def conn (d/connect client {:db-name "streque"}))

(def db (d/db conn))

(def entity-schema [{:db/ident :entity/type
                     :db/valueType :db.type/keyword
                     :db/cardinality :db.cardinality/one
                     :db/doc "Entity type"}])

(def entities [{:entity/type :entity/user}
               {:entity/type :entity/quote}])

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

(def test-users [{:user/id "u1"
                  :user/first-name "Jakob"
                  :user/last-name "Alfredsson"
                  :user/display-name "K-cob"}

                 {:user/id "u2"
                  :user/first-name "Test"
                  :user/last-name "Testsson"
                  :user/display-name "Tester"}])

(def test-quotes [{:quote/id "q1"
                   :quote/quote "Arbitrary quote 1..."
                   :quote/said-by "Jakob"
                   :quote/uploaded-by "u1"}

                  {:quote/id "q2"
                   :quote/quote "Arbitrary quote 2..."
                   :quote/said-by "Tester"
                   :quote/uploaded-by "u2"}])

(defn dissoc-db-id
  [entity]
  (dissoc entity :db/id))

(defn add-user!
  "Adds the user as specified by the user-map to the database that is connected via conn."
  [conn user-map]
  (d/transact conn {:tx-data [user-map]}))

(defn get-user
  "Gets the user with the given user-id from the database including :db/id."
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
  [conn uploading-user-id quote-map]
  (let [db (d/db conn)
        uploading-user (get-user db uploading-user-id)
        quote-map (merge quote-map {:quote/uploaded-by uploading-user})]
    (d/transact conn {:tx-data [quote-map]})))

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
  (d/create-database client {:db-name "streque"})
  (d/transact conn {:tx-data entity-schema})
  (d/transact conn {:tx-data entities})

  (d/transact conn {:tx-data user-schema})
  (d/transact conn {:tx-data test-users})
  (add-user! conn {:user/id "u3"
                   :user/display-name "Test"})
  (get-user db "u1")
  (d/pull db '[*] (get-user db "u1"))

  (d/transact conn {:tx-data quote-schema})
  (d/transact conn {:tx-data test-quotes})
  (add-quote! conn "u1" {:quote/id "q3" :quote/quote "Test-quote"})
  (get-quote db "q3")
  (d/pull db '[*] (get-quote db "q3"))
  (d/pull db '[:quote/_uploaded-by] (get-user db "u1"))
  (d/pull db '[* {[:quote/_uploaded-by] [:quote/id :quote/quote]}] (get-user db "u1"))
  (:db/id (:quote/uploaded-by (d/pull db '[:quote/uploaded-by] (get-quote db "q3"))))
  (d/pull db '[*] (:db/id (:quote/uploaded-by (d/pull db '[:quote/uploaded-by] (get-quote db "q3")))))



  (d/datoms db {:index :eavt})
  ; (get-all-entities db)
  (d/delete-database client {:db-name "streque"}))