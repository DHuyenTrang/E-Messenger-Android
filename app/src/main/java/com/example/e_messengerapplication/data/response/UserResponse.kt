package com.example.e_messengerapplication.data.response

data class UserResponse(
    val result: UserDto
) {
}

data class UserDto(
    val id: String,
    val phoneNumber: String,
    val dob: String,
    val displayName: String,
    val email: String,
)

/*
 "id": "67f4cf2df94bc041dec486a7",
    "phoneNumber": "0123456788",
    "password": "$2a$10$e3PhCxuge/T9ghaIfUR3lO9HOx.GgtYF1eto8bTwDqFbEoQTuptf2",
    "dob": "2004-04-04",
    "displayName": "String",
    "email": "string@gmail.com",
 */