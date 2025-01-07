(ns streque.unit-test-db
  (:require [datomic.client.api :as d]
            [streque.schemas :as schemas]))


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
(d/transact unit-test-connection {:tx-data schemas/user-schema})
(d/transact unit-test-connection {:tx-data schemas/quote-schema})
(d/transact unit-test-connection {:tx-data schemas/menu-item-schema})
(def unit-test-db (d/with-db unit-test-connection))
; --------------------------------------------------

(defn start-unit-test-db!
  []
  (d/create-database unit-test-client unit-test-db-arg-map)
  (d/transact unit-test-connection {:tx-data schemas/user-schema})
  (d/transact unit-test-connection {:tx-data schemas/quote-schema})
  (d/transact unit-test-connection {:tx-data schemas/menu-item-schema}))