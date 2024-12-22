(ns streque.http-server.http-server
  (:require [org.httpkit.server :as httpkit]
            [streque.http-server.end-points :refer [handler]]))

(defonce server-atom (atom nil))

(defn start-server!
  []
  (if (deref server-atom)
    "do nothing - already running"
    (do (println "starting server")
        (let [server-stop-fn (httpkit/run-server (fn [request]
                                                   (handler request))
                                                 {:port 9499})]
          (reset! server-atom server-stop-fn)))))

(defn stop-server!
  []
  (println "stopping server")
  (let [server-stop-fn (deref server-atom)]
    (if server-stop-fn
      (do (server-stop-fn :timeout 100)
          (reset! server-atom nil))
      "The server is not running")))

(comment
  (start-server!)
  (stop-server!))