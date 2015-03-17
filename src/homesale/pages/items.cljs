(ns homesale.pages.items
  (:require [re-frame.core :refer [subscribe dispatch]]))


(defn item-areas []
  (let [areas (subscribe [:item-areas])]
    (fn []
      [:ul.nav.nav-pills.nav-stacked
       (for [area @areas]
         [:li {:key (:key area)}
          [:a {:href "#"} (:name area)]])])))

(defn item-detail [item]
  [:p (str item)])

(defn item-list []
  (let [items (subscribe [:items])
        areas (subscribe [:area-map])
        levels (subscribe [:level-map])]
    (fn []
      [:div.item-list
       (for [item @items]
         [item-detail item])])))

(defn items-page []
  [:div#content.content
   [:h2 "Items"]
   [item-list]])
