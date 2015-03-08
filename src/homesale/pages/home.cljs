(ns homesale.pages.home
  (:require [re-frame.core :refer [subscribe dispatch]]))


(defn sale-levels []
  (let [levels (subscribe [:sale-levels])]
    (fn []
      [:p (str @levels)])))

(defn home-page []
  [:div#content.content
   [:h2 "Home Page"]
   [sale-levels]])
