(ns dev-db
  (:require [datomic.client.api :as d]
            [streque.schemas :as schemas]))

(def initial-local-dev-db [{:user/id "u1"
                            :user/first-name "Jakob"
                            :user/last-name "Alfredsson"
                            :user/display-name "K-cob"
                            :user/balance 200.0}

                           {:user/id "u2"
                            :user/first-name "Anton"
                            :user/last-name "Grensjö"
                            :user/display-name "Thotte"
                            :user/balance 1500.0}
                           
                           {:menu-item/id "mi1"
                            :menu-item/name "Öl"
                            :menu-item/price 11.0}])

; --------------------------------------------------
;               Start local dev-database
; --------------------------------------------------
(def client (d/client {:server-type :datomic-local
                                 :storage-dir :mem
                                 :system "ci"}))
(def local-dev-db-arg-map {:db-name "local-dev-db"})
(d/create-database client local-dev-db-arg-map)
(def connection (d/connect client local-dev-db-arg-map))
(d/transact connection {:tx-data schemas/user-schema})
(d/transact connection {:tx-data schemas/quote-schema})
(d/transact connection {:tx-data schemas/menu-item-schema})
(d/transact connection {:tx-data initial-local-dev-db})
(def db (d/db connection))
; --------------------------------------------------