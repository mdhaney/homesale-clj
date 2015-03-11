(ns homesale.server
  (:require [cljsjs.firebase]
            [re-frame.core :refer [dispatch]]))

(def fb-url "https://haneyhome.firebaseio.com")
(def base-ref (js/Firebase. fb-url))

;;
;; Helpers
;;
(defn child* [fbref & [path & paths]]
  (if-not path
    fbref
    (recur (.child fbref path) paths)))

(defn child [& paths]
  (apply child* base-ref paths))

;;
;; monitor firebase authentication
;;
(.onAuth base-ref
  (fn [auth-data]
    (if auth-data
      (dispatch [:fb-auth (js->clj (.-auth auth-data) :keywordize-keys true)])
      (dispatch [:fb-unauth]))))

;;
;; Firebase authentication
;;
(defn login! [email password]
  (.authWithPassword base-ref #js {:email email :password password}
                     (fn [error auth-data]
                       (dispatch [:fb-login-result error auth-data]))
                     #js {:remember "sessionOnly"}))

(defn logout! []
  (.unauth base-ref))

;;
;; Firebase handlers
;;
(doto (child "sale-level")
  (.on "child_added" #(dispatch [:fb-level-added %1]))
  (.on "child_changed" #(dispatch [:fb-level-changed %1]))
  (.on "child_removed" #(dispatch [:fb-level-removed %1])))

(doto (child "item-area")
  (.on "child_added" #(dispatch [:fb-area-added %1]))
  (.on "child_changed" #(dispatch [:fb-area-changed %1]))
  (.on "child_removed" #(dispatch [:fb-area-removed %1])))
