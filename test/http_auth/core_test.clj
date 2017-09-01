(ns http-users.core-test
  (:require [clojure.test :refer :all]
            [http-users.core :refer :all]))


(deftest a-test
  (testing "FIXME, I fail."
    (my-json-read user-data)
    (is (= 1 1))))

