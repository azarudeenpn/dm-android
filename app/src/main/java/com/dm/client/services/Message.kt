package com.dm.client.services

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
public class Message{


    @JsonField
    public var message: String = ""
}