(ns homesale.pages
  (:require [homesale.pages.home :refer [home-page]]
            [homesale.pages.add-items :refer [add-items-page]]))

(def pages {:home home-page
            :add-items add-items-page})
