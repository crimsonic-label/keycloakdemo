# Using Keycloak

https://www.baeldung.com/spring-boot-keycloak

## Docker-compose
Runs keycloak service with exposed `8081` port

login to administration console: `admin, admin`

## Create a realm
add a new realm called SpringBootKeycloak

## Creating a Client
click Create, call new Client `login-app`

set valid redirect URIs: `http://localhost:8088/*`, our applications runs on 8080 port

## Creating a Role and a User
create `user` role

create a user, set the initial password, assign role to the user
