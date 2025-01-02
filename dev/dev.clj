(ns dev
  (:require [dev-db]
            [test-db]
            [dev-http-server]
            [streque.edn-api]))

(defn initialize-dev-environment!
  [_]
  (dev-http-server/start-server!)
  (reset! streque.edn-api/db-atom dev-db/db))