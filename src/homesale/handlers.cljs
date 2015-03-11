(ns homesale.handlers
  (:require [re-frame.core :refer [register-handler dispatch]]
            [homesale.server :as server]))

;;
;; firebase authentication
;;
(register-handler :fb-auth
 (fn [db [_ authdata]]
   (assoc db :auth authdata)))

(register-handler :fb-unauth
 (fn [db _]
   (dissoc db :auth)))

;;
;; firebase child handlers
;;
(register-handler :fb-child-added
 (fn [db [_ kw snapshot]]
   (update-in db [kw] assoc (.key snapshot) (js->clj (.val snapshot) :keywordize-keys true))))

(register-handler :fb-child-changed
 (fn [db [_ kw snapshot]]
   (update-in db [kw] assoc (.key snapshot) (js->clj (.val snapshot) :keywordize-keys true))))

(register-handler :fb-child-removed
 (fn [db [_ kw snapshot]]
   (update-in db [kw] dissoc (.key snapshot))))

;;
;; init
;;
(register-handler :initialize-db
 (fn [db [_ pages]]
   {:pages pages
    :current-page :home}))

;;
;; navigation
;;
(register-handler :show-page
 (fn [db [_ page]]
   (assoc db :current-page page)))

;;
;; authentication
;;
(register-handler :login-request
 (fn [db [_ email password]]
   (server/login! email password)
   (assoc-in db [:login :processing?] true)))

(register-handler :logout
 (fn [db _]
   (server/logout!)
   db))

(register-handler :fb-login-result
 (fn [db [_ error _]]
   (-> db
       (assoc-in [:login :processing?] false)
       (assoc-in [:login :error] (when error
                                   (.-message error))))))

