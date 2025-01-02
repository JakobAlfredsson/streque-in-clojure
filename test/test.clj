(ns test
  (:require [clojure.test  :as t]
            [streque.schemas]
            [streque.edn-api]
            [streque.http-server.mapper]))

(defn run-all-tests
  [_]
  (t/run-all-tests))