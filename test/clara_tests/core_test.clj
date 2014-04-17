(ns clara-tests.core-test
  (:require [clojure.test :refer :all]
            [clara.rules :refer :all]
            [clara.rules.dsl :as dsl])

  (:import tk.skuro.Bean))

(deftest pojo-test
  (testing "Clara rule and query over POJOs"
    (let [;; to be used as a starting fact
          bean1 (Bean. "Initial")

          ;; to be inserted into the working memory by a matching rule
          bean2 (Bean. "Derived")

          ;; matches a Bean and inserts a new one
          rule    (dsl/parse-rule [[Bean [b] (= "Initial" (.getValue b))]]
                                  (do (println "Adding a bean")
                                      (insert! bean2)))

          ;; should find the Bean inserted by the above rule
          result  (dsl/parse-query []
                                   [[Bean [b] (= "Derived" (.getValue b))]])

          ;; creates a fresh session, inserts the initial Bean then fires the rules
          session (-> (mk-session [rule result])
                      (insert bean1)
                      (fire-rules))]

      (is (= #{bean2}
             (into #{} (query session result)))))))

;; FAIL in (pojo-test) (core_test.clj:29)
;; Clara rule and query over POJOs
;; expected: (= #{bean2} (into #{} (query session result)))
;;   actual: (not (= #{#<Bean tk.skuro.Bean@42bbc556>} #{{}}))
