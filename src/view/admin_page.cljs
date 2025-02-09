(ns view.admin-page
  (:require [reagent.core]))

(defn app-component
  [state]
  [:h1 "Admin"])

; https://figwheel.org/docs/extra_mains.html
; https://figwheel.org/docs/your_own_server.html
; https://figwheel.org/docs/your_own_page.html
; https://figwheel.org/config-options#ring-handler