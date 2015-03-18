(ns homesale.handlers
  (:require [re-frame.core :refer [register-handler dispatch]]
            [re-frame.middleware :refer [debug]]
            [homesale.server :as server]))

;;
;; handler middleware
;;
(defn log-ex [handler]
  (fn log-ex-handler
    [db v]
    (try
      (handler db v)
      (catch :default e
        (do
          (.error js/console e.stack)
          (throw e))))))


(def standard-middlewares [(when js/goog.DEBUG
                             [log-ex debug])])

;;
;; firebase authentication
;;
(register-handler
 :fb-auth
 standard-middlewares
 (fn [db [_ authdata]]
   (assoc db :auth authdata)))

(register-handler
 :fb-unauth
 standard-middlewares
 (fn [db _]
   (dissoc db :auth)))

;;
;; firebase child handlers
;;
(register-handler
 :fb-child-added
 standard-middlewares
 (fn [db [_ kw snapshot]]
   (update-in db [kw] assoc (.key snapshot) (js->clj (.val snapshot) :keywordize-keys true))))

(register-handler
 :fb-child-changed
 standard-middlewares
 (fn [db [_ kw snapshot]]
   (update-in db [kw] assoc (.key snapshot) (js->clj (.val snapshot) :keywordize-keys true))))

(register-handler
 :fb-child-removed
 standard-middlewares
 (fn [db [_ kw snapshot]]
   (update-in db [kw] dissoc (.key snapshot))))

;;
;; init
;;
(register-handler
 :initialize-db
 standard-middlewares
 (fn [db [_ pages]]
   {:pages pages
    :current-page :home}))

;;
;; navigation
;;
(register-handler
 :show-page
 standard-middlewares
 (fn [db [_ page]]
   (assoc db :current-page page)))

;;
;; authentication
;;
(register-handler
 :login-request
 standard-middlewares
 (fn [db [_ email password]]
   (server/login! email password)
   (assoc-in db [:login :processing?] true)))

(register-handler
 :logout
 standard-middlewares
 (fn [db _]
   (server/logout!)
   db))

(register-handler
 :fb-login-result
 standard-middlewares
 (fn [db [_ error _]]
   (-> db
       (assoc-in [:login :processing?] false)
       (assoc-in [:login :error] (when error
                                   (.-message error))))))

;;
;; new items
;;
(register-handler
 :new-item-name-changed
 standard-middlewares
 (fn [db [_ name]]
   (assoc-in db [:new-item :name] name)))

(register-handler
 :new-item-add
 standard-middlewares
 (fn [db _]
   (update-in db [:new-item] dissoc :name)))

