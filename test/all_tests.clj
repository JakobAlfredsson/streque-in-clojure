(ns all-tests
  (:require [clojure.test  :as t]
            [streque.db]
            [streque.edn-api]))

(defn run-all-tests
  [_]
  (t/run-all-tests))