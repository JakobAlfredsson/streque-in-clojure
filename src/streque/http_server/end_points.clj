(ns streque.http-server.end-points
  (:require [streque.edn-api :as edn-api]
            [clojure.data.json :as json]
            [streque.http-server.mapper :as mapper]))

(def cors-headers {"Access-Control-Allow-Origin"  "*"
                   "Access-Control-Allow-Methods" "*"})

(defn handler
  [request]
  (let [uri (:uri request)]
    (cond
      (= uri "/get-all-users")
      {:status 200
       :headers (merge cors-headers {"Content-Type" "application/json"})
       :body (as-> (edn-api/get-all-users) $
                  (map mapper/user->http-user $)
                  (json/write-str $ {:escape-unicode false}))}
      
      (= uri "/get-streque-menu")
      {:status 200
       :headers (merge cors-headers {"Content-Type" "application/json"})
       :body (as-> (edn-api/get-menu) $
                  (mapper/menu->http-menu $)
                  (json/write-str $ {:escape-unicode false}))}

      :else {:status  200
             :headers {"Content-Type" "text/html"}
             :body    ""})))