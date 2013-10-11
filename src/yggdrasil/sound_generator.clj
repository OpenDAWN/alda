(ns yggdrasil.sound-generator
  (:require [yggdrasil.parser :as parser])
  (:import (java.io File)))

(defn check-for
  "Checks to see if a given file already exists. If it does, prompts the user whether he/she wants to
   overwrite the file. If he/she doesn't, then prompts the user to choose a new filename and calls
   itself to check the new file, etc. Returns a filename that does not exist, or does exist and the
   user is OK with overwriting it."
  [filename]
  (letfn [(prompt [] 
            (print "> ") (read-line))
          (overwrite-dialog []
            (println "File \"" filename "\" already exists. Overwrite? (y/n)")
            (let [response (prompt)]
              (cond
                (some #{response} ["y" "yes" "Y" "YES" "Yes"])
                filename

                (some #{response} ["n" "no" "N" "NO" "No"])
                (do
                  (println "Please specify a different filename.")
                  (check-for (prompt)))

               :else
               (do 
                 (println "Answer the question, sir.")
                 (recur)))))]
    (cond 
      (.isFile (File. filename))
      (overwrite-dialog)

      (.isDirectory (File. filename))
        (do
          (println "\"" filename "\" is a directory. Please specify a filename.")
          (recur (prompt)))

      :else filename)))

(defn play
  "Parses an input file and plays the result, using the specified options."
  [input-file {:keys [start end]}]
  (comment "To do."))

(defn make-wav
  "Parses an input file and saves the resulting sound data as a wav file, using the
   specified options."
  [input-file output-file {:keys [start end]}]
  (let [target-file (check-for output-file)]
    (comment "To do.")))