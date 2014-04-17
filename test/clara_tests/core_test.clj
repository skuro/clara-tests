(ns clara-tests.core-test
  (:require [clojure.test :refer :all]
            [clara-tests.core :refer :all]
            [clara.rules :refer :all]
            [clara.rules.dsl :as dsl])

  (:import tk.skuro.Bean))

(deftest a-test
  (testing "Clara rule and query over POJOs"
    (let [bean1 (Bean. "Initial")
          bean2 (Bean. "Derived")

          rule    (dsl/parse-rule [[Bean [b] (= "Initial" (.getValue b))]]
                                  (do (println "Adding a bean")
                                      (insert! bean2)))
          result  (dsl/parse-query []
                                   [[Bean [b] (= "Derived" (.getValue b))]])
          session (-> (mk-session [rule result])
                      (insert bean1)
                      (fire-rules))]
      (is (= #{bean2} (into #{} (query session result))))))) ;; actual: (not (= \x ({})))
