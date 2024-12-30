(ns all-tests
  (:require [clojure.test  :as t]
            [streque.db]
            [streque.edn-api]
            [streque.http-server.mapper]))

(defn run-all-tests
  [_]
  (t/run-all-tests))