(ns todo-clj.handler.todo
  (:require [compojure.core :refer [defroutes context GET POST]]
            [todo-clj.db.todo :as todo]
            [todo-clj.util.response :as res]
            [todo-clj.view.todo :as view]))

(def todo-list
  [{:title "朝ごはんを作る"}
   {:title "燃えるごみを出す"}
   {:title "卵を買って帰る"}
   {:title "お風呂を洗う"}])

;; (defn todo-index-view [req]
;;   `("<h1>TODO 一覧</h1>"
;;     "<ul>"
;;     ~@(for [{:keys [title]} todo-list]
;;         (str "<li>" title "</li>"))
;;     "</ul>"))

(defn todo-index [req]
  ;; (let [todo-list (todo/find-todo-all)]
  (-> (view/todo-index-view req todo-list)
      res/response
      res/html))

(defn todo-new [req]
  (-> (view/todo-new-view req)
      res/response
      res/html))

(defn todo-new-post [{:as req :keys [params]}]
  (if-let [todo (first (todo/save-todo (:title params)))]
    (-> (res/redirect (str "/todo/" (:id todo)))
        (assoc :flash {:msg "TODOを正常に追加しました。"})
        res/html)))

(defn todo-search [req] "TODO search")
(defn todo-show [{:as req :keys [params]}]
  (if-let [todo (todo/find-first-todo (Long/parseLong (:todo-id params)))]  ;; paramsからidを取得→数値に変換→queryを取得してtodoとする
    (-> (view/todo-show-view req todo)
        res/response
        res/html)))

(defn todo-edit [req] "TODO edit")
(defn todo-edit-post [req] "TODO edit post")
(defn todo-delete [req] "TODO delete")
(defn todo-delete-post [req] "TODO delete post")

(defroutes todo-routes
  (context "/todo" _
    (GET "/" _ todo-index)
    (GET "/new" _ todo-new)
    (POST "/new" _ todo-new-post)
    (GET "/search" _ todo-search)
    (context "/:todo-id" _
      (GET "/" _ todo-show)
      (GET "/edit" _ todo-edit)
      (POST "/edit" _ todo-edit-post)
      (GET "/delete" _ todo-delete)
      (POST "/delete" _ todo-delete-post))))