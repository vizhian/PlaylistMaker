package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.api.NetworkClient
import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.data.dto.NetworkClientResponse
import com.practicum.playlistmaker.domain.api.SearchTrackRepository
import com.practicum.playlistmaker.domain.models.SearchResult
import com.practicum.playlistmaker.domain.models.Track

class SearchTrackRepositoryImpl(private val networkClient: NetworkClient) : SearchTrackRepository {

    override fun searchTracks(term: String): SearchResult<List<Track>> {
        when (val response = networkClient.getTracks(term)) {
            is NetworkClientResponse.Error -> return SearchResult.Error
            is NetworkClientResponse.Success -> {
                val tracks = response.data.map {
                    dtoToTrack(it)
                }
                return SearchResult.Success(tracks)
            }
        }
    }

    private fun dtoToTrack(trackDto: TrackDto): Track {
        return Track(
            trackDto.trackId,
            trackDto.trackName,
            trackDto.artistName,
            trackDto.trackTimeMillis,
            trackDto.artworkUrl100,
            trackDto.collectionName,
            trackDto.releaseDate,
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.previewUrl
        )
    }
}