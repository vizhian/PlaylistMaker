package com.practicum.playlistmaker.data.network

import android.util.Log
import com.practicum.playlistmaker.data.dto.NetworkClientResponse
import com.practicum.playlistmaker.data.api.NetworkClient
import com.practicum.playlistmaker.data.dto.TrackDto

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.IOException

class RetrofitItunesImpl(baseUrl: String): NetworkClient {
    val api = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create<ItunesApi>()

    override fun getTracks(term: String): NetworkClientResponse<List<TrackDto>> {
        try {
            val resp = api.getTrackList(term).execute()
            val body = resp.body()
            if (body == null) {
                Log.w("RetrofitImpl", "error trackListRequest, code: ${resp.code()}")
                return NetworkClientResponse.Error
            }
            return NetworkClientResponse.Success(body.results)
        } catch (e: IOException) {
            Log.w("RetrofitImpl", "error trackListRequest", e)
            return NetworkClientResponse.Error
        } catch (e: Exception) {
            Log.w("RetrofitImpl", "unexpected trackListRequest error", e)
            return NetworkClientResponse.Error
        }
    }
}