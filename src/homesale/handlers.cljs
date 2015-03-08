(ns homesale.handlers
  (:require [re-frame.core :refer [register-handler dispatch]]))

;; firebase - sale level
(register-handler
 :fb-level-added
 (fn [db [_ snapshot]]
   (update-in db [:sale-levels] assoc (.key snapshot) (js->clj (.val snapshot) :keywordize-keys true))))

(register-handler
 :fb-level-changed
 (fn [db [_ snapshot]]
   (update-in db [:sale-levels] assoc (.key snapshot) (js->clj (.val snapshot) :keywordize-keys true))))

(register-handler
 :fb-level-removed
 (fn [db [_ snapshot]]
   (update-in db [:sale-levels] dissoc (.key snapshot))))

;; firebase - item area
(register-handler
 :fb-area-added
 (fn [db [_ snapshot]]
   (update-in db [:item-areas] assoc (.key snapshot) (js->clj (.val snapshot) :keywordize-keys true))))

(register-handler
 :fb-area-changed
 (fn [db [_ snapshot]]
   (update-in db [:item-areas] assoc (.key snapshot) (js->clj (.val snapshot) :keywordize-keys true))))

(register-handler
 :fb-area-removed
 (fn [db [_ snapshot]]
   (update-in db [:item-areas] dissoc (.key snapshot))))

;; init
(register-handler
 :initialize-db
 (fn [db [_ pages]]
   {:pages pages
    :current-page :home}))

;; navigation
(register-handler
 :show-page
 (fn [db [_ page]]
   (assoc db :current-page page)))

