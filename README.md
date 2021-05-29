# BigBrother

Spring REST API for course project at HSE

## JWT

Authentication is done via Json Web Token. 

- JwtUser implements UserDetails (user from SpringSecurity), JwtUserDetailsService implements UserDetailsService;
- JwtTokenProvider generates token for every user;
- JwtTokenFilter  validates tokens and gives authentication;
- JwtConfigurer makes every request go through JwtTokenFilter;
- SecurityConfig configures requests, uses no session model, allows access users with roles to different endpoints.

Firstly, the user sends username and password and gets the token. Then he adds this token to the **header** of all his requests:

KEY: Authorization
VALUE: Bearer TOKEN_NUMBER

If token is invalid or expired, user has to resend his username and password to get a new token.

If user sends invalid or expired token empty response is returned.



## DTO

Data Transfer Objects are used for sending and receiving data.



## Table of contents:

### /auth

- [/login](#authlogin)

### /super_admin

- [/all_admins](#all_admins)
- [/add_admin](#add_admin)
- [/edit_admin](#edit_adminid10)
- [/delete_admin](#delete_adminid10)
- [/all_users](#all_users)

### /admin

- [/all_users](#all_users)
- [/user?id=3](#userid3)
- [/add_user](#add_user)
- [/edit_user](#edit_userid7)
- [/delete_user](#delete_userid7)
- [/all_attrs](#all_attrs)
- [/add_attr](#add_attr)
- [/edit_attr](#edit_attrid9)
- [/delete_attr](#delete_attrid7)
- [/add_attr_to_user](#add_attr_to_useruser_id5attr_id11)
- [/all_users_with_attr](#all_users_with_attrattr_id11)
- [/all_scripts](#all_scripts)
- [/add_count_script](#add_count_script)
- [/edit_count_script](#edit_count_scriptid9)
- [/add_interval_script](#add_interval_script)
- [/edit_interval_script](#edit_interval_scriptid9)
- [/delete_script](#delete_scriptid71)

### /user

- [/info](#info)
- [/scripts](#scripts)
- [/attrs](#attrs)

### /contact

- [/send_contact](#send_contact)

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

```json
{
    "status": "User with this username already exists"
}
```




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

##### Output on invalid id:

```json
{
    "status": "no user with id: 10"
}
```

#### 

#### /add_user

##### Type:

POST

##### Input:

```json
{
    "username":"testing_user",
    "firstName":"Bob",
    "lastName":"Uncle",
    "password":"testing_user"
}
```

##### Output on success:

```json
{
    "status": "OK",
    "username": "testing_user"
}
```

##### Output if user with this username exists:

```json
{
    "status": "User with this username already exists"
}
```

#### 

#### /edit_user?id=7

##### Type:

PUT

##### Input:

```json
{
    "username":"testing_user",
    "firstName":"Uncle",
    "lastName":"Bob",
    "password":"testing_user"
}
```

##### Output on success:

```json
{
    "id": 7,
    "username": "testing_user",
    "firstName": "Uncle",
    "lastName": "Bob"
}
```

##### Output if id is invalid:

```json
{
    "status": "no user with id: 10"
}
```

#### 

#### /delete_user?id=7

##### Type:

DELETE

##### Output on success:

```json
{
    "status": "OK"
}
```

##### Output if id is invalid:

```json
{
    "status": "No user with this id"
}
```







#### /all_attrs

##### Type:

GET

##### Output on success:

```json
[
    {
        "id": 9,
        "name": "testing_attr"
    },
    {
        "id": 11,
        "name": "cleaner"
    }
]
```



#### /add_attr

##### Type:

POST

##### Input:

```json
{
    "name":"testing_attr"
}
```

##### Output on success:

```json
{
    "attr_name": "testing_attr",
    "status": "OK"
}
```

#### 

#### /edit_attr?id=9

##### Type:

PUT

##### Input:

```json
{
    "name":"first_attr"
}
```

##### Output on success:

```json
{
    "attr_name": "first_attr",
    "status": "OK"
}
```

##### Output if id is invalid:

```json
{
    "status": "no attr with id: 13"
}
```

#### 

#### /delete_attr?id=7

##### Type:

DELETE

##### Output on success:

```json
{
    "status": "OK"
}
```

##### Output if id is invalid:

```json
{
    "status": "No attr with this id"
}
```

#### 

#### /add_attr_to_user?user_id=5&attr_id=11

##### Type:

POST

##### Output on success:

```json
{
    "status": "OK"
}
```

##### Output if id is invalid:

```json
{
    "status": "No user or attr with such id"
}
```

#### 

#### /all_users_with_attr?attr_id=11

##### Type:

GET

##### Output on success:

```json
[
    {
        "id": 5,
        "username": "testing_admin",
        "firstName": "John",
        "lastName": "Johns"
    },
    {
        "id": 4,
        "username": "user1",
        "firstName": "user1FirstName",
        "lastName": "user1LastName"
    }
]
```



#### /all_scripts

##### Type:

GET

##### Output:

```json
[
    {
        "id": 71,
        "scriptName": "edited_count_script",
        "id1": 1,
        "id2": 2,
        "count": 0
    },
    {
        "id": 72,
        "scriptName": "edited_interval_script",
        "id1": 5,
        "id2": 4,
        "from": 1000000,
        "to": 3000000,
        "success": false
    },
    {
        "id": 73,
        "scriptName": "brand_new_count_script",
        "id1": 5,
        "id2": 4,
        "count": 0
    }
]
```



#### /add_count_script

##### Type:

POST

##### Input:

```json
{
    "scriptName":"testing_count_script",
    "id1":"4",
    "id2":"5"
}
```

##### Output on success:

```json
{
    "script_name": "testing_count_script",
    "status": "OK"
}
```

#### 

#### /edit_count_script?id=9

##### Type:

PUT

##### Input:

```json
{
    "scriptName":"edited_count_script",
    "id1":"1",
    "id2":"2"
}
```

##### Output on success:

```json
{
    "script_name": "edited_count_script",
    "status": "OK"
}
```

##### Output if id is invalid:

```json
{
    "status": "no script with id: 234"
}
```

#### 

#### /add_interval_script

##### Type:

POST

##### Input:

```json
{
    "scriptName":"testing_interval_script",
    "id1":"1",
    "id2":"2",
    "from":"1000000",
    "to":"2000000"
}
```

##### Output on success:

```json
{
    "script_name": "testing_interval_script",
    "status": "OK"
}
```

#### 

#### /edit_interval_script?id=9

##### Type:

PUT

##### Input:

```json
{
    "scriptName":"edited_interval_script",
    "id1":"5",
    "id2":"4",
    "from":"1000000",
    "to":"3000000"
}
```

##### Output on success:

```json
{
    "script_name": "edited_interval_script",
    "status": "OK"
}
```

##### Output if id is invalid:

```json
{
    "status": "no script with id: 234"
}
```

#### 

#### /delete_script?id=71

##### Type:

DELETE

##### Output on success:

```json
{
    "status": "OK"
}
```

##### Output if id is invalid:

```json
{
    "status": "No script with this id"
}
```

#### 



### /user

#### /info

##### Type:

GET

##### Output on success:

```json
{
    "id": 4,
    "username": "basic_user",
    "firstName": "John",
    "lastName": "Snow"
}
```

#### 

#### /scripts

##### Type:

GET

##### Output on success:

```json
[
    {
        "id": 72,
        "scriptName": "edited_interval_script",
        "id1": 5,
        "id2": 4,
        "from": 1000000,
        "to": 3000000,
        "success": null
    },
    {
        "id": 73,
        "scriptName": "brand_new_count_script",
        "id1": 5,
        "id2": 4,
        "count": 0
    }
]
```

#### 

#### /attrs

##### Type:

GET

##### Output on success:

```json
[
    {
        "id": 11,
        "name": "cleaner"
    }
]
```

#### 



### /contact

#### /send_contact

##### Type:

POST

##### Input:

```json
{
    "id1":"5",
    "id2":"4",
    "time":"150000"
}
```

##### Output on success:

```json
{
    "status": "OK"
}
```

##### Output if id is invalid:

```json
{
    "status": "no users with such id"
}
```

##### Output if time < 0:

```json
{
    "status": "invalid time value"
}
```

#### 