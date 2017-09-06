(ns http-users.core
  (:gen-class))

(require '[org.httpkit.server :as http] 
         '[clojure.data.json :as json]
         '[hiccup.core :as h])

(def port 8080)
(def all-users (atom {:tero "passu" :pasi "ppaswd" :harri "wdp"}))
(def page-count (atom 0))


(defn print-users []
  (doseq [ user  @all-users ]
    (println (str (first user) " " (second user)))))

(defn users->table [users]
  [:table 
           [:tr [:td "User"] [:td "Password"] ]
            (for [user @all-users] 
              (let [k (first user)
                    v (second user)]
                [:tr [:td k ] [:td v] ]))])

(defn header [title]
  [:head [:title title ]])

(defn body [users]
  [:body 
   [:h2 "known users"]
   (users->table users) 
   [:br]
   [:p (str "Page accessed " (swap! page-count inc) " times") ]])

(defn html-page [title users]
  [:html 
   (header title)
   (body users)])

(defn http-reply [status-code headers body]
  {:status status-code
   :headers headers
   :body body })

(defn add-users-to-atom! [new-users]
  (doseq [user (get new-users "users")]
    (swap! all-users assoc (keyword (nth (first user) 1)) (nth (second user) 1))))
  
; json API:  
; # curl -X post --data '{"users":[{"user":"aaafaska","password":"aaapasswood"}, {"user":"aaayeeso","password":"aaaenkerro"}]}' http://localhost:8080 

(defn handle-post-new [req] 
  (let [ json-data (slurp (:body req)) 
         new-users (json/read-str json-data) ]
    (add-users-to-atom! new-users)
    (http-reply 200 {"Content-Type" "text/html"} 
                (h/html (html-page "System users" all-users)))))

(defn app [req]
  (if (= (:request-method req) :get)
    (http-reply 200 {"Content-Type" "text/html"} 
                (h/html (html-page "System users" all-users)))
    (handle-post-new req)))

(defn -main
  "List and add users"
  [& args]
  (println "Starting server in port " port)
  (http/run-server app {:port port}))
