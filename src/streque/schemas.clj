(ns streque.schemas
  (:require [clojure.walk]))

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
                              :user/last-name "G"
                              :user/display-name "Thotte"
                              :user/balance 1500.0}]))

