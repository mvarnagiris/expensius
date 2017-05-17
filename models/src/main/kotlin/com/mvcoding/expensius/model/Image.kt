package com.mvcoding.expensius.model

import java.io.Serializable

interface Image : Serializable
data class UriImage(val uri: String) : Image