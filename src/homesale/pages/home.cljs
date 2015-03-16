(ns homesale.pages.home
  (:require [re-frame.core :refer [subscribe dispatch]]))

(defn sale-level [level]
  [:li (str level)])

(defn sale-levels []
  (let [levels (subscribe [:sale-levels])]
    (fn []
      [:ul
       (for [level @levels]
         [sale-level level])])))

(defn home-page []
  [:div#content.content
   [:h2 "Home Page"]
   [sale-levels]])
