package com.practicum.playlistmaker

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {
    fun updateTrackList(term: String, onResult: (result: RepositoryResult) -> Unit) {
        RetrofitImpl.api.getTrackList(term).enqueue(object : Callback<TrackListResponse> {
            override fun onResponse(
                call: Call<TrackListResponse>,
                response: Response<TrackListResponse>
            ) {
                val trackList = response.body()?.results
                if (response.isSuccessful && trackList != null) {
                    onResult(RepositoryResult.Success(trackList))
                } else {
                    onResult(RepositoryResult.Error(response.code()))
                }
            }

            override fun onFailure(
                call: Call<TrackListResponse>,
                t: Throwable
            ) {
                onResult(RepositoryResult.Error(0))
            }
        })
    }
}

sealed class RepositoryResult {
    class Success(val trackList: List<Track>) : RepositoryResult()
    class Error(val code: Int) : RepositoryResult()
}