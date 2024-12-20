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

(def entities [{:entity/type :user}])

(def user-schema [{:db/ident :user/id
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/unique      :db.unique/identity
                   :db/doc "The id of the user"}

                ;;   {:db/ident :type
                ;;    :db/valueType :db.type/keyword
                ;;    :db/cardinality :db.cardinality/one
                ;;    :db/doc "The type of the entity. Should be :user"}

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

(def test-users [{:user/id "u1"
                  ;:type :user
                  :user/first-name "Jakob"
                  :user/last-name "Alfredsson"
                  :user/display-name "K-cob"}
                 {:user/id "u2"
                  ;:type :user
                  :user/first-name "Test"
                  :user/last-name "Testsson"
                  :user/display-name "Tester"}])

(def user-q '[:find (pull ?user [*])
              :in $ ?user-id
              :where [?user :user/id ?user-id]])

(defn get-user
  "Get the user with the given user-id from the database and trim away :db/id."
  [db user-id]
  (-> (d/q user-q db user-id)
      (ffirst)
      (dissoc :db/id)))

(def entities-q '[:find (pull ?entity [*])
                  :where [?entity :entity/type _]])

(defn get-all-entities
  [db])

;; (def all-entities-q '[:find (pull ?e [*])
;;                       :where [?e :user/first-name "Jakob"]])

;; (defn get-all-entities
;;   "Get all entities from the database and trim away :db/id."
;;   [db]
;;   (->> (d/q all-entities-q db)
;;        (flatten)
;;        (map (fn [user] (dissoc user :db/id)))))


(comment
  ; https://docs.datomic.com/client-tutorial/client.html
  ; https://docs.datomic.com/client-api/datomic.client.api.html
  ; https://docs.datomic.com/clojure/index.html#datomic.api/entity
  ; https://stackoverflow.com/questions/14189647/get-all-fields-from-a-datomic-entity
  (d/create-database client {:db-name "streque"})
  (d/transact conn {:tx-data entity-schema})
  (d/transact conn {:tx-data user-schema})
  (d/transact conn {:tx-data test-users})
  (get-user db "u1")
  ; (get-all-entities db)
  (d/delete-database client {:db-name "streque"}))