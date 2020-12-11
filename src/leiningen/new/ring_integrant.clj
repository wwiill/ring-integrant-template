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
    (->files data
             ["project.clj" (render "project.clj" data)]
             ["deps.edn" (render "deps.edn" data)]
             ["README.md" (render "README.md" data)]
             [".gitignore" (render "gitignore" data)]

             ["src/{{sanitized}}/app.clj" (render "app.clj" data)]
             ["src/{{sanitized}}/logging.clj" (render "logging.clj" data)]
             ["src/{{sanitized}}/main.clj" (render "main.clj" data)]
             ["src/{{sanitized}}/server.clj" (render "server.clj" data)]
             ["src/{{sanitized}}/store.clj" (render "store.clj" data)]

             ["resources/logback.xml" (render "logback.xml" data)]
             ["dev/dev.clj" (render "dev.clj" data)]
             ["test/{{sanitized}}/main_test.clj" (render "main_test.clj" data)])))
