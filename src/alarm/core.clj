(ns alarm.core
  (:require 
   [clojure.string :as str]
   [dynne.sampled-sound :as audio]
   )
  (:gen-class))

(def audio-read (audio/read-sound "resources/op.wav"))
(def one-piece-opening (atom nil))

(defn- get-time
  []
  (let [time (str (java.time.LocalTime/now))]
    (take 2 (str/split time #":"))))

(defn run-alarm
  []
  (println "! WAKE UP ! WAKE UP ! WAKE UP !")
  (let [ls '(0 1 2 3 4 5 6 7 8 9)
        password (reduce str (shuffle ls))]
    (println (str "Password: " password))
    (print "> ")
    (let [user-input (read-line)]
      (if (= user-input password) (audio/stop @one-piece-opening) (run-alarm)))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [alarm-off? (atom true)
        hour (first args)
        minutes (last args)
        current-hour (atom nil)
        current-minute (atom nil)]

    (while @alarm-off?
      (Thread/sleep 1000)
      (cond
        (and (= @current-hour hour) (= @current-minute minutes)) (reset! alarm-off? false)
        :else (do
                (println @current-hour)
                (println "waiting...")
                (reset! current-hour (first (get-time)))
                (reset! current-minute (last (get-time))))))
    (reset! one-piece-opening (audio/play audio-read))
    (run-alarm)))

