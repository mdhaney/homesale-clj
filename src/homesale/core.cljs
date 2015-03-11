(ns homesale.core
  (:require [homesale.server]
            [homesale.queries]
            [homesale.handlers]
            [homesale.pages.home :refer [home-page]]
            [homesale.pages.items :refer [items-page]]
            [homesale.ui.navbar :refer [navbar]]
            [secretary.core :as secretary :include-macros true :refer-macros [defroute]]
            [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
    (:import goog.History))

(enable-console-print!)

;;
;; Routes
;;
(secretary/set-config! :prefix "#")

(defroute home-route "/" [] (dispatch [:show-page :home]))
(defroute items-route "/items" [] (dispatch [:show-page :items]))
(defroute "*" [] (dispatch [:show-page nil]))


;; ----------
;; History
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))
;; need to run this after routes have been defined


;;
;; Page definitions
;;
(def pages {:home        {:render home-page
                          :text "Homesale"
                          :uri (home-route)}
            :items       {:render items-page
                          :text "Items"
                          :uri (items-route)
                          :nav-order 1}})

(defn login-page []
  (let [login-state (atom {:email "txmikester@gmail.com" :password ""})
        processing? (subscribe [:login-processing])
        error (subscribe [:login-error])]
    (fn []
      (let [{:keys [email password]} @login-state]
        [:form
         [:div.form-group
          [:input.form-control {:type "email"
                                :placeholder "Email address"
                                :value email
                                :on-change #(swap! login-state assoc :email (-> % .-target .-value))}]]
         [:div.form-group
          [:input.form-control {:type "password"
                                :placeholder "Password"
                                :value password
                                :on-change #(swap! login-state assoc :password (-> % .-target .-value))}]]
         [:div.login-buttons
          (if @processing?
            [:i.fa.fa-spinner.fa-spin.fa-2x]
            [:button.btn.btn-default
             {:on-click #(dispatch [:login-request (:email @login-state) (:password @login-state)] )}
             "Sign In"])]
         [:div.login-error
          (when @error
            [:p (str "Login error - " @error)])]]))))

(defn app-page []
  (let [current-page (subscribe [:current-page])
        auth (subscribe [:auth-data])]
    (fn []
      (if @auth
        (let [page-render (:render @current-page)]
          [:div.container-fluid
           [navbar pages current-page]
           (if page-render
             [page-render]
             [:h1 "404"])])
        [login-page]))))

(defn init! []
  (hook-browser-navigation!)
  (dispatch-sync [:initialize-db pages])
  (reagent/render [app-page] (.getElementById js/document "app")))

(init!)
