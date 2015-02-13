(ns homesale.core
  (:require [homesale.pages.home :refer [home-page]]
            [homesale.pages.add-items :refer [add-items-page]]
            [homesale.ui.navbar :refer [navbar]]
            [secretary.core :as secretary :include-macros true :refer-macros [defroute]]
            [reagent.core :as reagent :refer [atom]]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
    (:import goog.History))

(enable-console-print!)

; app state
(def db (atom {}))


;;
;; Routes
;;
(secretary/set-config! :prefix "#")

(defroute home-route "/" []
  (swap! db assoc :current-page :home))

(defroute add-items-route "/items/add" []
  (swap! db assoc :current-page :add-items))

(defroute "*" []
  (swap! db dissoc :current-page))


;; ----------
;; History
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))
;; need to run this after routes have been defined


;;
;; Page definitions
;;
(def pages {:home        {:render home-page
                          :text "Homesale"
                          :uri (home-route)}
            :add-items   {:render add-items-page
                          :text "Add Items"
                          :uri (add-items-route)
                          :nav-order 1}})

(defn app-page [db]
  (let [current-page (:current-page @db)
        page-render (get-in pages [current-page :render])]
    [:div.container
     [navbar pages current-page]
     (if page-render
       [page-render db]
       [:h1 "404"])]))


(defn init! []
  (hook-browser-navigation!)
  (reagent/render [app-page db] (.getElementById js/document "app")))

(init!)
