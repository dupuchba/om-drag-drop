(ns om-drag-drop.core
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(defmulti read om/dispatch)

(def init-state {:app/list-one [{:id   1
                                 :text "This is a draggable element 1"}
                                {:id   2
                                 :text "This is a draggable element 2"}
                                {:id   3
                                 :text "This is a draggable element 3"}
                                {:id   4
                                 :text "This is a draggable element 4"}
                                {:id   5
                                 :text "This is a draggable element 5"}
                                {:id   6
                                 :text "This is a draggable element 6"}
                                {:id   7
                                 :text "This is a draggable element 7"}
                                ]})

(defui Element
  static om/Ident
  (ident [this {:keys [id]}]
    [:element/by-id id])
  static om/IQuery
  (query [this]
    [:id :text])
  Object
  (render [this]
    (let [{:keys [id text]} (om/props this)]
      (dom/li #js {:draggable   true
                   :title       id
                   :onDragStart (fn [e]
                                  (if (instance? js/HTMLLIElement (.-target e))
                                    (let [dataTransfer (.-dataTransfer e)]
                                      (.setData dataTransfer "text/x-example" (str (om/get-ident this)))
                                      (set! (.-effectAllowed dataTransfer) "move"))
                                    (.preventDefault e)))}
              text))))

(def element (om/factory Element {:keyfn :id}))

(defui App
  static om/IQuery
  (query [this]
    [{:app/list-one (om/get-query Element)}])
  Object
  (render [this]
    (let [{:keys [app/list-one]} (om/props this)]
      (dom/div nil
               (dom/h1 nil "Hello world!")
               (dom/ol #js {:onDragEnter (fn [e]
                                           (.preventDefault e))
                            :onDragOver (fn [e]
                                          (set! (.. e -dataTransfer -dropEffect) "move")
                                          (.preventDefault e))
                            :onDrop (fn [e]
                                      (js/console.log (.getData (.-dataTransfer e) "text/x-example")))}
                       (map element list-one))))))

(defmethod read :default
  [{:keys [query state ast]} key params]
  (let [st @state]
    {:value (om/db->tree query (get st key) st)}))

(def parser (om/parser {:read read}))

(def reconciler (om/reconciler {:state  init-state
                                :parser parser}))

(om/add-root! reconciler App (.getElementById js/document "app"))
