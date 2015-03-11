(ns homesale.handlers
  (:require [re-frame.core :refer [register-handler dispatch]]
            [homesale.server :as server]))

;; firebase authentication
(register-handler
 :fb-auth
 (fn [db [_ authdata]]
   (assoc db :auth authdata)))

(register-handler
 :fb-unauth
 (fn [db _]
   (dissoc db :auth)))

;; firebase - users
(register-handler
 :fb-user-added
 (fn [db [_ snapshot]]
   (update-in db [:users] assoc (.key snapshot) (js->clj (.val snapshot) :keywordize-keys true))))

(register-handler
 :fb-user-changed
 (fn [db [_ snapshot]]
   (update-in db [:users] assoc (.key snapshot) (js->clj (.val snapshot) :keywordize-keys true))))

(register-handler
 :fb-user-removed
 (fn [db [_ snapshot]]
   (update-in db [:users] dissoc (.key snapshot))))

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

;; authentication
(register-handler
 :login-request
 (fn [db [_ email password]]
   (server/login! email password)
   (assoc-in db [:login :processing?] true)))

(register-handler
 :logout
 (fn [db _]
   (server/logout!)
   db))

(register-handler
 :fb-login-result
 (fn [db [_ error _]]
   (-> db
       (assoc-in [:login :processing?] false)
       (assoc-in [:login :error] (when error
                                   (.-message error))))))

