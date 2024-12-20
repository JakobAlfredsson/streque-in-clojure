(ns streque.db
  (:require
   [datomic.client.api :as d]))

; (require '[datomic.client.api :as d])
(def client (d/client {:server-type :datomic-local
                       :storage-dir :mem
                       :system "ci"}))

(def conn (d/connect client {:db-name "streque"}))

(def db (d/db conn))

(def user-schema [{:db/ident :user/user-id
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

(def test-users [{:user/user-id "u1"
                  :user/first-name "Jakob"
                  :user/last-name "Alfredsson"
                  :user/display-name "K-cob"}
                 {:user/user-id "u2"
                  :user/first-name "Test"
                  :user/last-name "Testsson"
                  :user/display-name "Tester"}])

(def all-users-q '[:find (pull ?e [*])
                   :where [?e :user/user-id _]
                   [?e ?a ?v]])

(defn get-all-users
  [db]
  (-> (d/q all-users-q db)
      (flatten)))

(defn get-user-by-id
  [user-id db]
  (let [all-users (d/q all-users-q db)]
    (filter (fn [user] (= user-id (:user-id user))) all-users)))

(comment
  ; https://docs.datomic.com/client-tutorial/client.html
  ; https://docs.datomic.com/client-api/datomic.client.api.html
  ; https://docs.datomic.com/clojure/index.html#datomic.api/entity
  ; https://stackoverflow.com/questions/14189647/get-all-fields-from-a-datomic-entity
  (d/create-database client {:db-name "streque"})
  (d/transact conn {:tx-data user-schema})
  (d/transact conn {:tx-data test-users})
  (d/q all-users-q db)
  (d/delete-database client {:db-name "streque"})
  )