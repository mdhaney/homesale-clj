(ns homesale.pages.items
  (:require [re-frame.core :refer [subscribe dispatch]]))


(defn item-areas []
  (let [areas (subscribe [:item-areas])]
    (fn []
      [:ul.nav.nav-pills.nav-stacked
       (for [area @areas]
         [:li {:key (:key area)}
          [:a {:href "#"} (:name area)]])])))

(defn new-item []
  (let [item (subscribe [:new-item])]
    (fn []
      (let [{:keys [name]} @item]
        [:form {:on-submit #(dispatch [:new-item-add])}
         [:div.row
          [:div.col-xs-12.col-sm-6
           [:input.form-control {:type "text"
                                 :placeholder "Description"
                                 :value name
                                 :on-change #(dispatch [:new-item-name-changed (-> % .-target .-value)])}]]
          [:div.col-xs-4.col-sm-2
           [:p "area"]]
          [:div.col-xs-4.col-sm-2
           [:p "level"]]
          [:div.col-xs-2.col-sm-2
           [:button.btn.btn-primary {:type "submit"} "Add Item"]]]]))))

(defn item-detail [item]
  [:p (str item)])

(defn item-list []
  (let [items (subscribe [:items])
        areas (subscribe [:area-map])
        levels (subscribe [:level-map])]
    (fn []
      [:div.item-list
       (for [item @items]
         [item-detail item])])))

(defn items-page []
  [:div#content.content
   [:h2 "Items"]
   [item-list]
   [new-item]])
