(ns todo-clj.db
  (:require [clojure.java.jdbc :as jdbc]
            [environ.core :refer [env]]))

(def db-spec
  (:db env))
  ;; {:dbtype "postgresql" :dbname "todo_clj_dev" :host "localhost" :port 5432 :user "username" :password "password"})
  ;; {:dbtype "postgresql" :dbname "todo_clj_dev" :host "localhost"})

(defn migrate []
  (jdbc/db-do-commands
   db-spec
   (jdbc/create-table-ddl :todo [:id :serial] [:title :varchar])))