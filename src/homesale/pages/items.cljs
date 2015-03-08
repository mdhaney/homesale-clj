(ns homesale.pages.items
  (:require [re-frame.core :refer [subscribe dispatch]]))

(defn item-areas []
  (let [areas (subscribe [:item-areas])]
    (fn []
      [:p (str @areas)])))

(defn items-page []
  [:div#content.content
   [:h2 "Items Page"]
   [item-areas]])
