(defproject
  {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/tools.logging "1.1.0"]
                 [ch.qos.logback/logback-classic "1.2.6"]
                 [ch.qos.logback.contrib/logback-json-classic "0.1.5"]
                 [ch.qos.logback.contrib/logback-jackson "0.1.5"]
                 [com.fasterxml.jackson.core/jackson-databind "2.12.5"]
                 [com.fasterxml.jackson.core/jackson-core "2.12.5"]
                 [integrant "0.8.0"]
                 [ring/ring-jetty-adapter "1.9.4"]
                 [metosin/reitit "0.5.15"]]

  :profiles {:dev     {:source-paths ["dev" "test"]
                       :dependencies [[integrant/repl "0.3.2" :scope "provided"]]
                       :repl-options {:init-ns dev}}
             :uberjar {:aot :all}}

  :main {{name}}.main
  :jar-name "{{name}}.jar"
  :uberjar-name "{{name}}-standalone.jar")
