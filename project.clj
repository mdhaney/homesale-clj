(defproject homesale "0.1.0-SNAPSHOT"
  :description "Home sale webapp"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2665"]
                 [reagent "0.5.0-alpha"]
                 [secretary "1.2.1"]]

  :plugins [[lein-cljsbuild "1.0.4"]
            [lein-httpd "1.0.0"]
            [lein-resource "14.10.1"]
            [lein-pdo "0.1.1"]
            [fsrun "0.1.2"]]

  :source-paths ["src"]

  :clean-targets [:target-path :compile-path "static" "dist"]

  :cljsbuild {:builds {:dev {:source-paths ["src"]
                             :compiler {:output-to "static/js/main.js"
                                        :output-dir "static/js"
                                        :optimizations :none
                                        :pretty-print true
                                        :source-map true}}
                       :prod {:source-paths ["src"]
                              :compiler {:externs  ["react/externs/react.js"
                                                    "externs/firebase.ext.js"]
                                         :preamble ["reagent/react.js"]
                                         :output-to "dist/js/main.min.js"
                                         :optimizations :advanced
                                         :pretty-print  false}}}}

  :profiles {:dev
             {:dependencies [[lein-light-nrepl "0.1.0"]]
              :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]}
              :resource {:resource-paths ["resources/public"]
                         :target-path "static"
                         :update false
                         :skip-stencil [ #"resources/public/assets/.*" ]
                         :extra-values {:scripts [{:src "assets/firebase.js"}
                                                  {:src "http://fb.me/react-0.12.2.js"}
                                                  {:src "js/goog/base.js"}
                                                  {:src "js/main.js"}
                                                  {:body "goog.require('homesale.core')"}]
                                        :repl true}}}
             :prod
             {:resource {:resource-paths ["resources/public"]
                         :target-path "dist"
                         :update false
                         :skip-stencil [ #"resources/public/assets/.*" ]
                         :extra-values {:scripts [{:src "assets/firebase.js"}
                                                  {:src "js/main.min.js"}]
                                        :repl false}}}}

  :aliases {"watch-html" ["fschange" "resources/public/*" "resource"]
            "watch-all"  ["pdo" "cljsbuild" "auto" "dev," "watch-html"]
            "dist" ["with-profile" "prod" "do" "cljsbuild" "once" "prod," "resource"]})

