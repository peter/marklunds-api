# Versioned Example

This is an example Clojure app to showcase how to use the [versioned](https://github.com/peter/versioned)
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
(users/create (:app system) {:name "Admin User" :email "admin@example.com" :password "admin"})
exit
```

Start the server:

```
lein run
```

In a different terminal, log in:

```bash
curl -i -X POST -H 'Content-Type: application/json' -d '{"email": "admin@example.com", "password": "admin"}' http://localhost:5000/v1/login

export TOKEN=<token in header response above>
```

Basic CRUD workflow:

```bash
# create
curl -i -X POST -H 'Content-Type: application/json' -H "Authorization: Bearer $TOKEN" -d '{"data": {"attributes": {"title": {"se": "My Section"}, "slug": {"se": "my-section"}}}}' http://localhost:5000/v1/sections

# get
curl -i -H "Authorization: Bearer $TOKEN" http://localhost:5000/v1/sections/1

# list
curl -i -H "Authorization: Bearer $TOKEN" http://localhost:5000/v1/sections

# update
curl -i -X PUT -H 'Content-Type: application/json' -H "Authorization: Bearer $TOKEN" -d '{"data": {"attributes": {"title": {"se": "My Section EDIT"}}}}' http://localhost:5000/v1/sections/1

# delete
curl -i -X DELETE -H "Authorization: Bearer $TOKEN" http://localhost:5000/v1/sections/1
```

## Dropbox

```
(require '[app.core :as marklunds])
(def system (marklunds/-main :start-web false))
(require '[app.dropbox :as dropbox :reload-all true])
(dropbox/save (:app system) {:type "foobar" :id 1 :body "foobar"})
```
