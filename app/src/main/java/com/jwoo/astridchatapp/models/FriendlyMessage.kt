package com.jwoo.astridchatapp.models

class FriendlyMessage() {
    var fromId: String? = null
    var toId: String? = null
    var text: String? = null
    var name: String? = null

    constructor(fromId: String, toId: String, text: String, name: String) : this() {
        this.fromId = fromId
        this.toId = toId
        this.text = text
        this.name = name
    }
}