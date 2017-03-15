(ns om-drag-drop.core
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [cognitect.transit :as t]
            [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

;(def init-state {:app/list-one [{:id   1
;                                 :text "This is a draggable element 1"}
;                                {:id   2
;                                 :text "This is a draggable element 2"}
;                                {:id   3
;                                 :text "This is a draggable element 3"}
;                                {:id   4
;                                 :text "This is a draggable element 4"}
;                                {:id   5
;                                 :text "This is a draggable element 5"}
;                                {:id   6
;                                 :text "This is a draggable element 6"}
;                                {:id   7
;                                 :text "This is a draggable element 7"}]
;                 :app/list-two [{:id   8
;                                 :text "This is a draggable element 8"}
;                                {:id   9
;                                 :text "This is a draggable element 9"}
;                                {:id   10
;                                 :text "This is a draggable element 10"}]})
;
;(defui Element
;  static om/Ident
;  (ident [this {:keys [id]}]
;    [:element/by-id id])
;  static om/IQuery
;  (query [this]
;    [:id :text])
;  Object
;  (render [this]
;    (let [{:keys [id text]} (om/props this)
;          {:keys [from]} (om/get-computed this)]
;      (dom/li #js {:draggable   true
;                   :title       id
;                   :onDragStart (fn [e]
;                                  (if (instance? js/HTMLLIElement (.-target e))
;                                    (let [dataTransfer (.-dataTransfer e)]
;                                      (.setData dataTransfer "text/x-example" (str (om/get-ident this)))
;                                      (set! (.-effectAllowed dataTransfer) "move")
;                                      (om/transact! this
;                                                    `[(element/drag {:from    ~from
;                                                                     :element ~(om/get-ident this)})
;                                                      :app/list-one :app/list-two]))
;                                    (.preventDefault e)))}
;              text))))
;
;(def element (om/factory Element {:keyfn :id}))
;
;(defui App
;  static om/IQuery
;  (query [this]
;    [{:app/list-one (om/get-query Element)}
;     {:app/list-two (om/get-query Element)}
;     :elements/dragged])
;  Object
;  (render [this]
;    (let [{:keys [app/list-one app/list-two elements/dragged]} (om/props this)]
;      (js/console.log "Title")
;      (js/console.log (om/props this))
;      (dom/div nil
;               (dom/h3 nil "List one !")
;               (dom/ol #js {:onDragEnter (fn [e]
;                                           (.preventDefault e))
;                            :onDragOver  (fn [e]
;                                           (set! (.. e -dataTransfer -dropEffect) "move")
;                                           (.preventDefault e))
;                            :onDrop      (fn [e]
;                                           #_(om/transact! this
;                                                         `[(list/move-element ~(into dragged {:to :app/list-one}))
;                                                           :app/list-one :app/list-two]))}
;                       (map #(element (om/computed % {:from :app/list-one})) list-one))
;
;               (dom/h3 nil "List two !")
;               (dom/ol #js {:onDragEnter (fn [e]
;                                           (.preventDefault e))
;                            :onDragOver  (fn [e]
;                                           (set! (.. e -dataTransfer -dropEffect) "move")
;                                           (.preventDefault e))
;                            :onDrop      (fn [e]
;                                           #_(om/transact! this
;                                                         `[(list/move-element ~(into dragged {:to :app/list-two}))
;                                                           :app/list-one :app/list-two]))}
;                       (map #(element (om/computed % {:from :app/list-two})) list-two))))))
;
;(defmulti read om/dispatch)
;(defmulti mutate om/dispatch)
;
;(defmethod read :default
;  [{:keys [query state ast]} key params]
;  (let [st @state]
;    {:value (om/db->tree query (get st key) st)}))
;
;(defmethod mutate 'element/drag
;  [{:keys [state]} _ params]
;  {:value {:keys [:elements/dragged]}
;   :action (fn []
;             (if-not (empty? params)
;               (swap! state assoc :elements/dragged params)
;               (swap! state assoc :elements/dragged nil)))})
;
;(defmethod mutate 'list/move-element
;  [{:keys [state]} _ {:keys [to from element]}]
;  {:value {:keys [:app/list-one :app/list-two]}
;   :action (fn []
;             (js/console.log to)
;             (js/console.log from))})
;
;(def parser (om/parser {:read read :mutate mutate}))
;
;(def reconciler (om/reconciler {:state  init-state
;                                :parser parser}))

;;(om/add-root! reconciler App (.getElementById js/document "app"))













(def init-data {:app/lists [{:id 1
                             :elements [{:id   1
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
                                         :text "This is a draggable element 7"}]}
                            {:id 2
                             :elements [{:id   8
                                         :text "This is a draggable element 8"}
                                        {:id   9
                                         :text "This is a draggable element 9"}]}]})

(defui Element
  static om/Ident
  (ident [this {:keys [id]}]
    [:element/by-id id])
  static om/IQuery
  (query [this]
    [:id :text])
  Object
  (render [this]
    (let [{:keys [id text]} (om/props this)
          {:keys [on-drag-start]} (om/get-computed this)]
      (dom/li #js {:draggable   true
                   :title       id
                   :onDragStart (fn [e]
                                  (if (instance? js/HTMLLIElement (.-target e))
                                    (let [dataTransfer (.-dataTransfer e)]
                                      (.setData dataTransfer "text/x-example" (str (om/get-ident this)))
                                      (set! (.-effectAllowed dataTransfer) "move")
                                      (on-drag-start (om/get-ident this)))
                                    (.preventDefault e)))}
              text))))

(def element (om/factory Element {:keyfn :id}))

(defui ElementList
  static om/Ident
  (ident [this {:keys [id]}]
    [:list/by-id id])
  static om/IQuery
  (query [this]
    [:id {:elements (om/get-query Element)}])
  Object
  (on-drag-start [this element]
    (om/transact! this `[(element/drag {:from ~(om/get-ident this) :element ~element})]))
  (render [this]
    (js/console.log (om/props this))
    (let [{:keys [id elements]} (om/props this)]
      (dom/div nil
               (dom/h4 nil (str "List with id : " id))
               (dom/ul nil
                       (map (fn [el]
                              (element (om/computed el {:on-drag-start #(.on-drag-start this %)})))
                            elements))))))

(def element-list (om/factory ElementList {:keyfn :id}))

(defui App
  static om/IQuery
  (query [this]
    [{:app/lists (om/get-query ElementList)}])
  Object
  (render [this]
    (let [{:keys [app/lists]} (om/props this)]
      (dom/div nil
               (dom/h1 nil "This is where all it begins !")
               (map element-list lists)))))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [query state ast]} key params]
  (let [st @state]
    {:value (om/db->tree query (get st key) st)}))

(defmulti mutate om/dispatch)

(defmethod mutate 'element/drag
  [{:keys [state]} _ params]
  {:value {:keys [:elements/dragged]}
   :action (fn []
             (if-not (empty? params)
               (swap! state assoc :elements/dragged params)
               (swap! state assoc :elements/dragged nil)))})

(def parser (om/parser {:read read :mutate mutate}))

(def reconciler (om/reconciler {:state init-data :parser parser}))

(om/add-root! reconciler App (.getElementById js/document "app"))
