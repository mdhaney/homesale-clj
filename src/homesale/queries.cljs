(ns homesale.queries
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(defn merge-keys [m]
  (mapv
   (fn [[k v]]
     (assoc v :key k))
   m))

(register-sub :auth-user
  (fn [db _]
    (let [auth-data (reaction (:auth @db))
          users (reaction (:users @db))]
      (reaction (when (and @users @auth-data)
                  (let [uid (:uid @auth-data)]
                    (-> @users
                        (get uid)
                        (assoc :uid uid))))))))

(register-sub :sale-levels
 (fn [db _]
   (reaction (merge-keys (:sale-levels @db)))))

(register-sub :current-page
 (fn [db _]
   (let [pages (reaction (:pages @db))
         current-page (reaction (:current-page @db))]
     (reaction (get @pages @current-page)))))

(register-sub :item-areas
  (fn [db _]
    (let [areas (reaction (merge-keys (:item-areas @db)))]
      (reaction (sort-by :name @areas)))))

(register-sub :login-processing
  (fn [db _]
    (reaction (get-in @db [:login :processing?]))))

(register-sub :login-error
  (fn [db _]
    (reaction (get-in @db [:login :error]))))



