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
  (let [items (subscribe [:items])]
    (fn []
      [:div#content.content
       [:h2 "Items"]
       [:p (str @items)]])))
