(ns dev
  (:require [dev-db]
            [dev-http-server]
            [streque.edn-api]
            [figwheel.main :as figwheel]))

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

(comment
  (initialize-backend-environment! {}))