(ns homesale.ui.navbar
  (:require [reagent.core :refer [atom]]
            [re-frame.core :refer [subscribe dispatch]]))

(defn navlinks [pages current]
  (->> pages
     (map (fn [[kw item]]
            (-> item
                (assoc :key kw)
                (assoc :active? (= kw current)))))
     (filter :nav-order)
     (sort-by :nav-order)))

(defn navbar-brand [{:keys [text uri]}]
  [:a.navbar-brand {:href uri} text])

(defn collapse-button [collapsed?]
  [:button.navbar-toggle {:type "button"
                          :class (if @collapsed? "collapsed" "")
                          :on-click (fn [e]
                                      (swap! collapsed? not)
                                      nil)}
   [:span.sr-only "Toggle navigation"]
   [:span.icon-bar]
   [:span.icon-bar]
   [:span.icon-bar]])

(defn navbar [page-defs current-page]
  (let [collapsed? (atom true)
        user (subscribe [:auth-user])]
    (fn [page-defs current-page]
      (let [home-page (:home page-defs)]
        [:nav.navbar.navbar-default
         [:div.container-fluid
          [:div.navbar-header
           [collapse-button collapsed?]
           [navbar-brand home-page]]
          [:div.collapse.navbar-collapse {:class (if @collapsed? "" "in")}
           [:ul.nav.navbar-nav
            (for [{:keys [text uri active?]} (navlinks page-defs current-page)]
              [:li {:key text
                    :class (if active? "active" "")}
               [:a {:href uri
                    :on-click (fn [e]
                                (reset! collapsed? true)
                                nil)}
                text]])]
           [:span.pull-right
            [:p.navbar-text (str "Signed in as " @user)]
            [:button.btn.btn-primary.navbar-btn {:on-click #(dispatch [:logout])} "Logout"]]]]]))))
