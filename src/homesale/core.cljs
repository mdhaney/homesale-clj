(ns homesale.core
  (:require [homesale.routes :as routes]
            [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)


(defn app-page [db]
  (let [router (:router @db)
        page (:current-page router)]
    (if page
      [page db]
      [:h1 "404"])))

; app state
(def db (atom {}))

(defn init! []
  (routes/app-routes db)
  (reagent/render [app-page db] (.getElementById js/document "app")))

(init!)
