(ns homesale.routes
    (:require [secretary.core :as secretary :include-macros true :refer-macros [defroute]]
              [homesale.pages :refer [pages]]
              [goog.events :as events]
              [goog.history.EventType :as EventType])
    (:import goog.History))

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

;; ----------
;; Routes
(defn app-routes [db]
  (secretary/set-config! :prefix "#")

  (defroute "/" []
    (swap! db assoc
           :router {:current-page (pages :home)
                    :nav "home"}))

  (defroute "/items/add" []
    (swap! db assoc
           :router {:current-page (pages :add-items)
                    :nav "add-items"}))

  (defroute "*" []
    (swap! db dissoc :router))

  (hook-browser-navigation!))
