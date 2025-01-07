(ns dev
  (:require [dev-db]
            [dev-http-server]
            [streque.edn-api]))

(defn initialize-dev-environment!
  [_]
  (dev-http-server/start-server!)
  (reset! streque.edn-api/db-atom dev-db/db))