(ns boot-hedge.core
  (:require [boot.core          :as c]))

(def SUPPORTED_CLOUDS [:aws :azure])

; This task is only visible if this namespace
; is required without running init! for proper
; task import
(c/deftask HEDGE-README
  "WARNING: use core/init! to import Hedge tasks" 
  []
  (println "WARNING: use core/init! to import Hedge tasks")
  identity)

; The help task for Hedge
(c/deftask hedge-help
  "Hedge Help"
  []
  (println "\n\nTasks marked with ** are main tasks for end-users. You can find out more of a function by example:")
  (println "$> boot azure/deploy --help")
  (println ".. Would display help of function"))

(defn hedge-init! [& {:keys [clouds]
                      :or {clouds SUPPORTED_CLOUDS}}]
  (ns-unmap 'boot-hedge.core 'HEDGE-README)
  ; TODO: how to unmap HEDGE-README if user requires this file with :refer :all
  (doseq [cloud clouds]
    (case cloud
      :aws (require '[boot-hedge.aws.core :as aws])
      :azure (require '[boot-hedge.azure.core :as azure]))))
