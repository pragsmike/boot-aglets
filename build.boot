(set-env!
 :dependencies '[[jss "4.0"]])

(require '[boot.pod :as pod] '[boot.core :as core] '[clojure.java.io :as io])

(deftask copy-dependencies "Copy dependency jars to target directory." []
  (let [env (core/get-env)
        deps (:dependencies env)
        jars (->> deps (map (comp io/file #(pod/resolve-dependency-jar env %))))
        tmp (core/tmp-dir!)
        ]
    (dorun (map #(io/copy % (io/file tmp (.getName %))) jars))
    (core/with-pre-wrap fileset
      (-> fileset
          (core/add-resource tmp)
          core/commit!))))
