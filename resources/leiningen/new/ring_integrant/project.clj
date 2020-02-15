(defproject
  {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [integrant "0.8.0"]
                 [org.eclipse.jetty/jetty-util "9.4.20.v20190813"]
                 [org.eclipse.jetty/jetty-http "9.4.20.v20190813"]
                 [org.eclipse.jetty/jetty-server "9.4.20.v20190813"]
                 [ring/ring-jetty-adapter "1.8.0"]
                 [ring-middleware-format "0.7.4"]
                 [compojure "1.6.1"]
                 [org.clojure/tools.logging "0.5.0"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [ch.qos.logback.contrib/logback-jackson "0.1.5"]
                 [ch.qos.logback.contrib/logback-json-classic "0.1.5"]
                 [com.fasterxml.jackson.core/jackson-databind "2.10.2"]
                 [com.fasterxml.jackson.core/jackson-core "2.10.2"]]

  :profiles {:dev     {:source-paths ["dev" "test"]
                       :dependencies [[integrant/repl "0.3.1" :scope "provided"]]
                       :repl-options {:init-ns dev}}
             :uberjar {:aot :all}}

  :main {{name}}.main
  :jar-name "{{name}}.jar"
  :uberjar-name "{{name}}-standalone.jar")
