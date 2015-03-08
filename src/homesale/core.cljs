(ns homesale.core
  (:require [homesale.pages.home :refer [home-page]]
            [homesale.pages.add-items :refer [add-items-page]]
            [homesale.ui.navbar :refer [navbar]]
            [secretary.core :as secretary :include-macros true :refer-macros [defroute]]
            [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
    (:import goog.History))

(enable-console-print!)

;;
;; Routes
;;
(secretary/set-config! :prefix "#")

(defroute home-route "/" [] (dispatch [:show-page :home]))
(defroute add-items-route "/items/add" [] (dispatch [:show-page :add-items]))
(defroute "*" [] (dispatch [:show-page nil]))


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

(defn app-page []
  (let [current-page (subscribe [:current-page])]
    (fn []
      (let [page-render (:render @current-page)]
        [:div.container
         [navbar pages current-page]
         (if page-render
           [page-render]
           [:h1 "404"])]))))

(defn init! []
  (hook-browser-navigation!)
  (dispatch-sync [:initialize-db pages])
  (reagent/render [app-page] (.getElementById js/document "app")))

(init!)
