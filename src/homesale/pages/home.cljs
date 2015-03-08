(ns homesale.pages.home
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [cljsjs.firebase]
            [re-frame.core :refer [register-sub register-handler subscribe dispatch]]
            [reagent.core :refer [atom]]))

(def fb-url "https://haneyhome.firebaseio.com")
(def base-ref (js/Firebase. fb-url))

(def levels (atom {}))

(defn child* [fbref & [path & paths]]
  (if-not path
    fbref
    (recur (.child fbref path) paths)))

(defn child [& paths]
  (apply child* base-ref paths))

(defn fb-error [error]
  (println (str "Firebase error - " error)))

(def fbref (child "sale-level"))

(.on fbref "child_added" #(dispatch [:fb-level-added %1]))
(.on fbref "child_changed" #(dispatch [:fb-level-changed %1]))
(.on fbref "child_removed" #(dispatch [:fb-level-removed %1]))

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


(register-handler
 :initialize-db
 (fn [_ _]
   {}))

(register-sub
 :sale-levels
 (fn [db _]
   (reaction (mapv (fn [[k v]]
                     (assoc v :key k))
                   (:sale-levels @db)))))

(defn sale-levels []
  (let [levels (subscribe [:sale-levels])]
    (fn []
      [:p (str @levels)])))

(defn home-page [db]
  [:div#content.content
   [:h2 "Home Page"]
   [sale-levels]])
