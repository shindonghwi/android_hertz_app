package mago.apps.data.network.utils

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun <T : Any> safeApiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        Log.d("SafeApiRequest", "safeApiRequest: $response")
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val responseError = response.errorBody()?.string()
            val message = StringBuilder()
            responseError?.let {
                try {
                    message.append(JSONObject(it).getString("error"))
                } catch (e: JSONException) {
                    Log.d("SafeApiRequest", "safeApiRequest: error $e")
                }
            }
            Log.d("SafeApiRequest", "safeApiRequest: $message")
            throw ApiException(message.toString())
        }
    }
}