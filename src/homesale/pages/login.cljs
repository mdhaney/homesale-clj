(ns homesale.pages.login
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [subscribe dispatch]]))

(defn login-page []
  (let [login-state (atom {:email "txmikester@gmail.com" :password ""})
        processing? (subscribe [:login-processing])
        error (subscribe [:login-error])]
    (fn []
      (let [{:keys [email password]} @login-state]
        [:div.page-container
         [:div.login-container
          (when @error
            [:div.error
             [:p (str @error)]])
          [:form {:on-submit (fn [e]
                               (.preventDefault e)
                               (dispatch [:login-request (:email @login-state) (:password @login-state)]))}
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
           [:div
            (if @processing?
              [:div.text-center [:i.fa.fa-spinner.fa-spin.fa-2x]]
              [:button.btn.btn-default.center-block
               {:type "submit"}
               "Login"])]]]]))))
