package com.jwoo.astridchatapp.models

class Users() {
    var displayName: String? = null
    var image: String? = null
    var status: String? = null
    var thumbnail_image: String? = null

    constructor(
        displayName: String,
        image: String,
        status: String,
        thumbnail_image: String
    ) : this() {
        this.displayName = displayName
        this.image = image
        this.status = status
        this.thumbnail_image = thumbnail_image
    }
}