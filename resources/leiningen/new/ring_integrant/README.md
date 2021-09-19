# {{name}}

## Installation

## Usage

Run the project directly:

    $ clojure -m {{name}}.main

Run the project's tests (they'll fail until you edit them):

    $ clojure -A:test:runner

Build an uberjar:

    $ clojure -A:uberjar

Run that uberjar:

    $ java -jar logger.jar

## Examples

    $ curl --header "Accept: application/json" :9000/things

## Libraries

https://github.com/weavejester/integrant
https://github.com/ring-clojure/ring
https://github.com/ring-clojure/ring/tree/master/ring-jetty-adapter
https://github.com/metosin/reitit

## License

Copyright © 2020 {{name}}

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
