package com.khaled.devbytes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.khaled.devbytes.database.VideosDatabase
import com.khaled.devbytes.database.asDomainModel
import com.khaled.devbytes.domain.Video
import com.khaled.devbytes.network.Network
import com.khaled.devbytes.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideosRepository(private val database: VideosDatabase) {

    val videos: LiveData<List<Video>> = database.videoDao.getVideos().map {
        it.asDomainModel()
    }

    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            val playlist = Network.devBytes.getPlaylist().await()
            database.videoDao.insertAll(*playlist.asDatabaseModel())
        }
    }
}