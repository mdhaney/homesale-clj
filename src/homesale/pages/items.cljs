(ns homesale.pages.items
  (:require [re-frame.core :refer [subscribe dispatch]]))




(defn item-areas []
  (let [areas (subscribe [:item-areas])]
    (fn []
      [:ul.nav.nav-pills.nav-stacked
       (for [area @areas]
         [:li {:key (:key area)}
          [:a {:href "#"} (:name area)]])])))

(defn items-page []
  [:div#content.content
   [:div.row
    [:div.col-md-3
     [item-areas]]
    [:div.col-md-9
     [:h2 "Items"]]]])
