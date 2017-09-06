(ns http-users.core
  (:gen-class))

(require '[org.httpkit.server :as http] 
         '[clojure.data.json :as json]
         '[clojure.java.io :as io]
         '[clojure.data.csv :as csv]
         '[clojure.java.io :as io]
         '[hiccup.core :as h])

;(def user-data "/var/www/json/user-data.json")
;(def user-data (.getFile (clojure.java.io/resource "user-data.json")))

(def user-data (io/file (io/resource "user-data.json")))
(def port 8080)

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

;; (defn handle-post [req] 
;;   (let [ req-data (slurp (:body req)) 
;;          new-user (json/read-str req-data) 
;;          old-users (my-json-read user-data) 
;;          all-users (conj (get old-users "users") new-user) 
;;          all-as-json (json/write-str (assoc-in old-users ["users"] all-users)) ]
;;     (spit user-data all-as-json)
;;     (http-reply 200 {"Content-Type" "text/text"} 
;;                 (json->html (my-json-read user-data)))))

; #######################################
; # new implementation
; #######################################

(def users (atom {:tero "passu" :pasi "ppaswd" :harri "wdp"}))

(defn add-user! [name pwd]
  (swap! users assoc (keyword name) pwd))

(defn print-users []
  (doseq [ user  @users ]
    (println (str (first user) " " (second user)))))

(defn json->atom [users-map]
  (for [user (get users-map "users")]
    (add-user! (nth (first user) 1) (nth (second user) 1))))


(defn users->table [users]
  [:table 
           [:tr [:td "User"] [:td "Password"] ]
            (for [user @users] 
              (let [k (first user)
                    v (second user)]
                [:tr [:td k ] [:td v] ]))])

(defn header [title]
  [:head [:title title ]])
(defn body [users]
  [:body 
   [:h2 "known users"]
   (users->table users) 
   [:br]])
(defn html-page [title users]
  [:html 
   (header title)
   (body users)])

(defn http-reply [status-code headers body]
  {:status status-code
   :headers headers
   :body body })

(defn users-to-map [users-map] 
  (loop [my-users (get users-map "users")
         html {}]
    (if (empty? my-users)
      (let [ my-user (first my-users) ]
        (recur (rest my-users) (assoc html (keyword (first my-user))
                                      (second my-user)))))))


(defn handle-post-new [req] 
  (let [ json-data (slurp (:body req)) ]
    (json->atom (json/read-str json-data))
    (print-users)
    (http-reply 200 {"Content-Type" "text/html"} 
                (h/html (html-page "System users" users)))))

(defn app [req]
  (if (= (:request-method req) :get)
    (http-reply 200 {"Content-Type" "text/html"} 
                (h/html (html-page "System users" users)))
    (handle-post-new req)))

(defn -main
  "List and add user to /var/www/json/user-data.json"
  [& args]
  (println "Starting server in port " port)
  (http/run-server app {:port port}))
