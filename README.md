# marklunds-api

This the content API that serves the marklunds Node web app or www.marklunds.com.
This API is based on the [versioned](https://github.com/peter/versioned)
framework. The versioned framework provides a CMS REST API based on MongoDB with user authentication, JSON schema validation, versioning, publishing, and relationships.

## Getting Started

First make sure you have [Leiningen/Clojure](http://leiningen.org) and Mongodb installed.

Get the source:

```bash
git clone git@github.com:peter/versioned-example.git
cd versioned-example
```

Create an admin user:

```
lein repl
(require '[app.core :as marklunds])
(def system (marklunds/-main :start-web false))
(require '[versioned.models.users :as users])
(users/create (:app system) {:name "Peter Marklund" :email "admin@example.com" :password "admin" :permission "write"})
exit
```

Start the server:

```
lein run
```

In a different terminal, log in:

```bash
export BASE_URL=http://localhost:5000

export BASE_URL=https://marklunds-api.herokuapp.com

curl -i -X POST -H 'Content-Type: application/json' -d '{"email": "peter@marklunds.com", "password": ""}' $BASE_URL/v1/login

export TOKEN=<token in header response above>
```

Basic CRUD workflow:

```bash
# create
curl -i -X POST -H 'Content-Type: application/json' -H "Authorization: Bearer $TOKEN" -d '{"data": {"attributes": {"title": "hello world"}}}' $BASE_URL/v1/blog_posts

# get
curl -i -H "Authorization: Bearer $TOKEN" $BASE_URL/v1/blog_posts/1

# list
curl -i -H "Authorization: Bearer $TOKEN" $BASE_URL/v1/blog_posts

# update
curl -i -X PUT -H 'Content-Type: application/json' -H "Authorization: Bearer $TOKEN" -d '{"data": {"attributes": {"title": "hello world EDIT"}}}' $BASE_URL/v1/blog_posts/1

# delete
curl -i -X DELETE -H "Authorization: Bearer $TOKEN" $BASE_URL/v1/blog_posts/1
```

## Dropbox

```
(require '[app.core :as marklunds])
(def system (marklunds/-main :start-web false))
(require '[app.dropbox :as dropbox :reload-all true])
(dropbox/save (:app system) {:type "foobar" :id 1 :body "foobar"})
```

## Get a Local Checkout of Versioned Library

To checkout the versioned library (if you haven't already):

```
cd <your-parent-src-dir>
git clone https://github.com/peter/versioned <your-src-dir>/versioned
```

Then, in this repository, do:

```
mkdir checkouts
ln -s <your-src-dir>/versioned checkouts/versioned
```

Now you can make changes in your local versioned checkout and they will be reflected in this app.

## Importing Blog Posts

```
(require '[app.core :as marklunds])
(def system (marklunds/-main :start-web false))
(def app (:app system))
(require '[versioned.util.date :as d])
(require '[versioned.model-api :as model-api])
(require '[cheshire.core :as json])
(def file-path "/Users/peter/Dropbox/data/marklunds-postgresql-2018-02.json")
(def lines (as-> file-path v
                (slurp v)
                (clojure.string/replace v "\\\\" "\\")
                (clojure.string/split-lines v)))
; (seq (char-array (nth lines 0)))
(defn parse-date [date-str]
  (let [parsable-str (if (re-matches #".+\.\d\d\d$" date-str)
                       (str date-str "Z")
                       (str date-str ".000Z"))]
    (d/parse-datetime parsable-str)))
(defn parse-line [line]
  (as-> line v
        (json/parse-string v)
        ; id subject body comments_count created_at
        (clojure.set/rename-keys v {"inserted_at" "legacy_created_at"})
        (clojure.walk/keywordize-keys v)
        (merge v {:created_by "peter@marklunds.com"})
        (assoc v :legacy_created_at (parse-date (:legacy_created_at v)))))
(def docs (map parse-line lines))
(for [doc docs]
  (let [result (model-api/create app (get-in app [:models :blog_posts]) doc)]
    (println result)))
; 327 blog posts
```

## Import Diary Entries

```
(require '[app.core :as marklunds])
(def system (marklunds/-main :start-web false))
(def app (:app system))
(require '[versioned.util.date :as d])
(require '[versioned.model-api :as model-api])
(require '[cheshire.core :as json])
(def file-path "/Users/peter/Dropbox/data/savorings-diary-postgresql-2018-02.json")
(def lines (as-> file-path v
                (slurp v)
                (clojure.string/replace v "\\\\" "\\")
                (clojure.string/split-lines v)))
(defn parse-date [date-str]
  (let [parsable-str (if (re-matches #".+\.\d\d\d$" date-str)
                       (str date-str "Z")
                       (str date-str ".000Z"))]
    (d/parse-datetime parsable-str)))
(defn parse-line [line]
  (as-> line v
        (json/parse-string v)
        ; id subject body comments_count created_at
        (clojure.set/rename-keys v {"inserted_at" "legacy_created_at"})
        (clojure.walk/keywordize-keys v)
        (merge v {:created_by "peter@marklunds.com"})
        (assoc v :legacy_created_at (parse-date (:legacy_created_at v)))
        (dissoc v :updated_at)))
(def docs (map parse-line lines))
(for [doc docs]
  (let [result (model-api/create app (get-in app [:models :diary]) doc)]
    (println result)))
; 1037 diary entries (count lines)
```
