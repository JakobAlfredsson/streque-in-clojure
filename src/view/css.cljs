(ns view.css)

; Color palette generator
; https://coolors.co/27187e-758bfd-aeb8fe-f1f2f6-ff8600

(def dark-color
  "#000000")

(def medium-color
  "#44474e")

(def medium-color-darker
  "#383b40")

(def medium-color-lighter
  "#50535c")

(def light-color
  "#999999")

(def background-color
  "#ffffff")

(def accent-color
  "#ffd801")

(def accent-color-darker
  "#cdae00")

(def accent-color-lighter
  "#ffe034")

(def border-radius
  "0.4em 0.4em 0.4em 0.4em")

(def padding
  "0.4em")

(def default-shadow
  "0px 3px 3px 0px rgba(0,0,0,0.6)")

(def pressed-down-shadow
  "0px 1px 1px 0px rgba(0,0,0,0.6)")

(def default-card
  {:background-color light-color
   :margin-top "0.5rem"
   :height "150px"
   :width "100%"
   :display "flex"
   :color dark-color
   :padding padding
   :border-radius border-radius
   :flex-direction "row"
   :box-shadow default-shadow})

(def default-button
  {:padding padding
   :color dark-color
   :background-color accent-color
   :border "none" 
   :border-radius border-radius 
   :transition "background-color 0.2s"
   :box-shadow (str default-shadow
                ",0 3px " accent-color-darker)})

(defn default-button-hover
  [style style-hover-key]
  (if (style style-hover-key)
    {:background-color accent-color-lighter}
    {}))

(defn default-button-pressed
  [style style-pressed-key]
  (if (style style-pressed-key)
    {;:box-shadow "inset 0.2em 0.2em 0.8em"
     :box-shadow (str pressed-down-shadow
                  ",0 1px " accent-color-darker)
     :transform "translateY(2px)"}
    {}))

(def general-header
  {:height "10%"
   :text-align "center"
   :color background-color
   :display "inline-block"
   :background-color medium-color
   :left 0
   :top 0
   :width "100%"
   :box-shadow default-shadow
   :text-shadow default-shadow})

(def general-footer
  {:height "5%"
   :position "fixed"
   :background-color medium-color
   :left 0
   :bottom 0
   :width "100%"
   :box-shadow default-shadow})