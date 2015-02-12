(ns homesale.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(defn app-page []
  [:h1 "Hello"])

(defn init! []
  (reagent/render [app-page] (.getElementById js/document "app")))

(init!)
