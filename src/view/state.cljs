(ns view.state)

(defonce state-atom (atom {}))

(comment
  @state-atom
  (reset! state-atom {}))