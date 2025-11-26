package com.example.spotfix.data.remote

import android.content.Context
import com.example.spotfix.util.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Interface for saving/retrieving cookies
class CookieInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val prefs = context.getSharedPreferences(Constants.SESSION_PREFS, Context.MODE_PRIVATE)

        // 1. Attach stored cookie to outgoing request
        val cookie = prefs.getString(Constants.COOKIE_KEY, null)
        val newRequest = if (cookie != null) {
            request.newBuilder().header("Cookie", cookie).build()
        } else {
            request
        }

        val response = chain.proceed(newRequest)

        // 2. Save new cookie from incoming response (e.g., after login/register)
        if (response.headers("Set-Cookie").isNotEmpty()) {
            val sessionCookie = response.headers("Set-Cookie").firstOrNull { it.startsWith("connect.sid") }
            if (sessionCookie != null) {
                prefs.edit().putString(Constants.COOKIE_KEY, sessionCookie).apply()
            }
        }
        return response
    }
}

object RetrofitClient {
    fun getInstance(context: Context): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(CookieInterceptor(context)) // Add cookie interceptor
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}