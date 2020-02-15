(ns {{name}}.logging
  (:require
    [clojure.tools.logging :as log])
  (:import
    [org.slf4j MDC]))

(defmacro with-logging-context [context & body]
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
  `(with-logging-context ~ctx
                         (log/debug ~@args)))

(defmacro info [ctx & args]
  `(with-logging-context ~ctx
                         (log/info ~@args)))

(defmacro warn [ctx & args]
  `(with-logging-context ~ctx
                         (log/warn ~@args)))

(defmacro error [ctx & args]
  `(with-logging-context ~ctx
                         (log/error ~@args)))

(defmacro d
  "Convenience function to log a value (optionally with context) and return
  the value. Probably most useful for debugging scenarios."
  ([value]
   `(let [v# ~value]
      (debug v#)
      v#))
  ([context value] `(with-logging-context ~context (d ~value))))