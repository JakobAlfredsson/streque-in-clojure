(ns streque.core
  (:require [clojure.test :refer [is]]))

(defn print-hello
  {:test (fn []
           (is (= 1 1) "1 is 1"))}
  []
  {:a 1
   :b 2})

(print-hello)