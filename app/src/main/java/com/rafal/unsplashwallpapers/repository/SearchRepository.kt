package com.rafal.unsplashwallpapers.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rafal.unsplashwallpapers.model.UnsplashApi
import com.rafal.unsplashwallpapers.model.UnsplashCollection
import com.rafal.unsplashwallpapers.model.UnsplashSearchPhoto
import com.rafal.unsplashwallpapers.model.UnsplashUser
import com.rafal.unsplashwallpapers.model.pagingsource.CollectionsPagingSource
import com.rafal.unsplashwallpapers.model.pagingsource.PhotosPagingSource
import com.rafal.unsplashwallpapers.model.pagingsource.UsersPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: UnsplashApi
) {

    fun searchPhotos(query: String, orderBy: String): Flow<PagingData<UnsplashSearchPhoto>> {
        return Pager(
            PagingConfig(
                enablePlaceholders = false,
                pageSize = 10,
                maxSize = 100
            )
        ) {
            PhotosPagingSource(api, query, orderBy)
        }.flow
    }

    fun searchUsers(query: String): Flow<PagingData<UnsplashUser>> {
        return Pager(
            PagingConfig(
                enablePlaceholders = false,
                pageSize = 10,
                maxSize = 100
            )
        ) {
            UsersPagingSource(api, query)
        }.flow
    }

    fun searchCollections(query: String): Flow<PagingData<UnsplashCollection>> {
        return Pager(
            PagingConfig(
                enablePlaceholders = false,
                pageSize = 10,
                maxSize = 100
            )
        ) {
            CollectionsPagingSource(api, query)
        }.flow
    }

}