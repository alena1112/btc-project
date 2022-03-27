package com.anymind.btc.resources.dto

data class RestResponse(val response: String) {
    companion object {
        val OK = RestResponse("OK")
    }
}