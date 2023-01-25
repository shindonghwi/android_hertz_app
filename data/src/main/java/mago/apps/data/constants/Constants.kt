package mago.apps.data.constants

import mago.apps.data.BuildConfig

val BASE_URL = if (BuildConfig.DEBUG) {
    "https://hertz-api-dev.mago52.com"
} else {
    "https://hertz-api-dev.mago52.com"
}

const val API_VERSION = "v1"
const val HEADER_KEY = "Application-Client-ID"
const val HEADER_VALUE = "hertz-android-1"
const val HEADER_AUTH_KEY = "Authorization"
var HEADER_AUTH_VALUE = ""
