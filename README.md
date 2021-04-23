# BigBrother

Spring REST API for course project at HSE

## JWT

Authentication is done via Json Web Token. Firstly, the user sends username and password and gets the token. Then he adds this token to the **header** of all his requests:

KEY: Authorization \
VALUE: Bearer TOKEN_NUMBER

If token is invalid or expired, user has to resend his username and password to get a new token.

## Table of contents:

### /auth

- [/login](#authlogin)

### /super_admin

- [/all_admins](#all_admins)
- [/add_admin](#add_admin)
- [/edit_admin](#edit_adminid10)
- [/delete_admin](#delete_adminid10)

### /admin

- [/all_users](#all_users)
- [/user?id=3](#userid3)

## Endpoints:

### /auth

#### /auth/login

##### Type:
POST

##### Input:

```json
{
    "username":"super_admin",
    "password":"super_admin"
}
```

##### Output on success:

```json
{
    "username": "super_admin",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlcl9hZG1pbiIsImlhdCI6MTYxNzUzNDM5MywiZXhwIjoxNjE3NTM3OTkzfQ.Avxn1qGiazoXJ9yiZIG702meT76Ww26WNpEvhIs2JTc"
}
```

##### Output on failure:

```json
{
    "status": "Exception while authenticating: Invalid username or password"
}
```



### /super_admin

#### /all_admins

##### Type:
GET

##### Output on success:

```json
[
    {
        "id": 1,
        "username": "root",
        "firstName": "root",
        "lastName": "root"
    },
    {
        "id": 2,
        "username": "root2",
        "firstName": "Mike",
        "lastName": "Smith"
    }
]
```

##### Output on invalid token:

TODO


#### /add_admin

##### Type:
POST

##### Input:

```json
{
    "username":"new_admin",
    "firstName":"Mike",
    "lastName":"Johns",
    "password":"new_admin",
    "status":"ACTIVE"
}
```

##### Output on success:

```json
{
    "status": "OK",
    "username": "new_admin"
}
```

##### Output on failure:

TODO


#### /edit_admin?id=10

##### Type:
PUT

##### Input:

```json
{
    "username":"new_admin",
    "firstName":"Mike",
    "lastName":"Brown",
    "password":"new_admin",
    "status":"ACTIVE"
}
```

##### Output on success:

```json
{
    "username":"new_admin",
    "firstName":"Mike",
    "lastName":"Brown",
    "password":"new_admin",
    "status":"ACTIVE"
}
```

##### Output on invalid id:

```json
{
    "status": "no user with id: 10"
}
```


#### /delete_admin?id=10

##### Type:
DELETE

##### Output on success:

```json
{
    "status": "OK"
}
```

##### Output on invalid id:

```json
{
    "status": "no user with id: 10"
}
```


### /admin

#### /all_users

##### Type:
GET

##### Output on success:

```json
[
    {
        "id": 1,
        "username": "root",
        "firstName": "root",
        "lastName": "root"
    },
    {
        "id": 2,
        "username": "root2",
        "firstName": "Mike",
        "lastName": "Smith"
    },
    {
        "id": 3,
        "username": "notroot1",
        "firstName": "name1",
        "lastName": "last_name1"
    }
]
```

#### /user?id=3

##### Type:
GET

##### Output on success:

```json
{
    "id": 2,
    "username": "notroot",
    "firstName": "notroot",
    "lastName": "notroot"
}
```
