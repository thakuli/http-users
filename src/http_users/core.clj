(ns http-users.core
  (:gen-class))

(require '[org.httpkit.server :as http] 
         '[clojure.data.json :as json]
         '[clojure.java.io :as io]
         '[clojure.data.csv :as csv]
         '[clojure.java.io :as io])

(def user-data "/var/www/json/user-data.json")



(defn user-to-html [user]
  (str "<li>" (get user "user") ":" (get user "password") "</li>\n"))

(defn users-to-html [users-map] 
  (loop [my-users (get users-map "users")
         html ""]
    (if (empty? my-users)
      (str "<ul>" (clojure.string/join html) "</ul>")
      (let [ my-user (first my-users) ]
        (recur (rest my-users) (concat html (user-to-html my-user)))))))

(defn my-json-read [file] 
  (json/read (java.io.FileReader. file)))

(defn json->html [json]
  (let [ users-list (users-to-html json) ]
    (str "<html><head><title>users</title></head><body>" users-list 
         "</body></html>")))

(defn http-reply [status-code headers body]
  {:status status-code
   :headers headers
   :body body })

(defn handle-post [req] 
  (let [ req-data (slurp (:body req)) 
         new-user (json/read-str req-data) 
         old-users (my-json-read user-data) 
         all-users (conj (get old-users "users") new-user) 
         all-as-json (json/write-str (assoc-in old-users ["users"] all-users)) ]
    (spit user-data all-as-json)
    (http-reply 200 {"Content-Type" "text/text"} 
                (json->html (my-json-read user-data)))))


(defn app [req]
  (if (= (:request-method req) :get)
    (http-reply 200 {"Content-Type" "text/html"} 
                (json->html (my-json-read user-data)))
    (handle-post req)))

(defn -main
  "List and add user to /var/www/json/user-data.json"
  [& args]
  (http/run-server app {:port 8080}))
