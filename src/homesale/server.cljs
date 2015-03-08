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
