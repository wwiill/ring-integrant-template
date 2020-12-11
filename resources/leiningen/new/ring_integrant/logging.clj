(ns {{name}}.logging
  (:require
    [clojure.tools.logging :as log])
  (:import
    [org.slf4j MDC LoggerFactory]
    [ch.qos.logback.classic Logger Level]))

(defn reset-root-log-level
  ([level]
   (reset-root-log-level nil level))
  ([logger-name level]
   (if-let [logger ^Logger (.getLogger (LoggerFactory/getILoggerFactory)
                                       (or logger-name Logger/ROOT_LOGGER_NAME))]
           (.setLevel logger (Level/valueOf level))
           (log/warn "No logger called " logger-name " found, not changing level"))))

(defmacro with-mdc-context [context & body]
  `(let [wrapped-context# ~context
         ctx# (MDC/getCopyOfContextMap)]
     (try
       (if (map? wrapped-context#)
         (doall (map (fn [[k# v#]] (MDC/put (name k#) (str v#))) wrapped-context#)))
       ~@body
       (finally
         (if ctx#
           (MDC/setContextMap ctx#)
           (MDC/clear))))))

(defmacro debug [ctx & args]
  `(with-mdc-context ~ctx
                         (log/debug ~@args)))

(defmacro info [ctx & args]
  `(with-mdc-context ~ctx
                         (log/info ~@args)))

(defmacro warn [ctx & args]
  `(with-mdc-context ~ctx
                         (log/warn ~@args)))

(defmacro error [ctx & args]
  `(with-mdc-context ~ctx
                         (log/error ~@args)))

(defmacro d
  "Convenience function to log a value (optionally with context) and return
  the value. Probably most useful for debugging scenarios."
  ([value]
   `(let [v# ~value]
      (debug v#)
      v#))
  ([context value] `(with-mdc-context ~context (d ~value))))