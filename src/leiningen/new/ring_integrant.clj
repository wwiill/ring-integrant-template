(ns leiningen.new.ring-integrant
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "ring-integrant"))

(defn ring-integrant
  "FIXME: write documentation"
  [name]
  (let [data {:name      name
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' ring-integrant project.")
    (doseq [f ["main.clj" "http.clj" "logging.clj" "routes.clj" "store.clj"]]
      (->files data [(str "src/{{sanitized}}/" f) (render f data)]))
    (->files data ["README.md" (render "README.md" data)])
    (->files data ["deps.edn" (render "deps.edn" data)])
    (->files data [".gitignore" (render "gitignore" data)])
    (->files data ["resources/logback.xml" (render "logback.xml" data)])
    (->files data ["dev/dev.clj" (render "dev.clj" data)])
    (->files data ["project.clj" (render "project.clj" data)])
    (->files data [(str "test/{{sanitized}}/main_test.clj") (render "main_test.clj" data)])))
