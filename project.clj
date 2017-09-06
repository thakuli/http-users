(defproject http-users "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]  
                 [http-kit "2.2.0"]
                 [org.clojure/data.json "0.2.6"] 
                 [org.clojure/data.csv "0.1.4"] 
                 [hiccup "1.0.5"] ]
  :main ^:skip-aot http-users.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :plugins [[cider/cider-nrepl "0.15.0"]])
