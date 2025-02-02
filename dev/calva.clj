;; This file was an attempt at making Calva start an http-server, figwheel, etc.
;; It didn't really work out

(ns calva
  (:require [dev-db]
            [dev-http-server]
            [streque.edn-api]
            [figwheel.main :as figwheel]
            [nrepl.server]
            [nrepl.cmdline]
            [cider.nrepl]
            [cider.piggieback]))

(defn initialize-backend-environment!
  [_]
  (dev-http-server/start-server!)
  (reset! streque.edn-api/db-atom dev-db/db))

(defn initialize-frontend-environment!
  [_]
  (figwheel/-main "--build" "dev"))

(defn initialize-fullstack-environment!
  [_]
  (initialize-backend-environment! {})
  (initialize-frontend-environment! {}))

(defn -main
  [_]
  (let [nrepl-server (nrepl.server/start-server :handler cider.nrepl/cider-nrepl-handler)]  ; :handler [(nrepl.server/default-handler cider.nrepl/cider-middleware)] (nrepl.server/default-handler cider.piggieback/wrap-cljs-repl)
    (println "nREPL server started on port" (:port nrepl-server) "on host localhost - nrepl://localhost:" (:port nrepl-server))
    (initialize-fullstack-environment! {})
    ;(initialize-backend-environment! {})
    ))