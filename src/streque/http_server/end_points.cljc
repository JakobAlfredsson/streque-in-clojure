(ns streque.http-server.end-points)

(defn handler
  [request]
  (let [uri (:uri request)]
    (cond
      :else {:status  200
             :headers {"Content-Type" "text/html"}
             :body    "<h1>hello</h1>"})))