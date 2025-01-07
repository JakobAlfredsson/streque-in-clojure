(ns streque.schemas
  (:require [datomic.client.api :as d]
            [clojure.walk]))

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
                   :db/doc "The display name of the user"}

                  {:db/ident :user/balance
                   :db/valueType :db.type/float
                   :db/cardinality :db.cardinality/one
                   :db/doc "The balance of the user"}])

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

(def menu-item-schema [{:db/ident :menu-item/id
                        :db/valueType :db.type/string
                        :db/cardinality :db.cardinality/one
                        :db/unique      :db.unique/identity
                        :db/doc "The id of the menu-item"}
                       {:db/ident :menu-item/name
                        :db/valueType :db.type/string
                        :db/cardinality :db.cardinality/one
                        :db/doc "The name of the menu-item"}
                       {:db/ident :menu-item/price
                        :db/valueType :db.type/float
                        :db/cardinality :db.cardinality/one
                        :db/doc "The price of the menu-item"}])

(comment
  (def initial-local-dev-db [{:user/id "u1"
                              :user/first-name "Jakob"
                              :user/last-name "Alfredsson"
                              :user/display-name "K-cob"
                              :user/balance 200.0}

                             {:user/id "u2"
                              :user/first-name "Anton"
                              :user/last-name "Grensjö"
                              :user/display-name "Thotte"
                              :user/balance 1500.0}])
  

; --------------------------------------------------
;               Start local dev-database
; --------------------------------------------------
  (def local-dev-client (d/client {:server-type :datomic-local
                                   :storage-dir :mem
                                   :system "ci"}))
  
  (def local-dev-db-arg-map {:db-name "local-dev-db"})
  
  (d/create-database local-dev-client local-dev-db-arg-map)
  
  (def local-dev-connection (d/connect local-dev-client local-dev-db-arg-map))
  
  (d/transact local-dev-connection {:tx-data user-schema})
  
  (d/transact local-dev-connection {:tx-data quote-schema})
  
  (d/transact local-dev-connection {:tx-data initial-local-dev-db})
  
  (def local-dev-db (d/db local-dev-connection))
  
; --------------------------------------------------
  
; --------------------------------------------------
;            Start database for unit tests
; --------------------------------------------------
  (def unit-test-client (d/client
                         {:server-type :datomic-local
                          :storage-dir :mem
                          :system "ci"}))
  
  (def unit-test-db-arg-map {:db-name "unit-test-db"})
  
  (d/create-database unit-test-client unit-test-db-arg-map)
  
  (def unit-test-connection (d/connect unit-test-client unit-test-db-arg-map))
  
  (d/transact unit-test-connection {:tx-data user-schema})
  
  (d/transact unit-test-connection {:tx-data quote-schema})
  
  (def unit-test-db (d/with-db unit-test-connection))
  )
; --------------------------------------------------
