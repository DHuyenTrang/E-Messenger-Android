package com.example.e_messengerapplication.data.request

data class CreateGroupRequest(
    val groupName: String,
    val participantIds: List<String>
) {
}