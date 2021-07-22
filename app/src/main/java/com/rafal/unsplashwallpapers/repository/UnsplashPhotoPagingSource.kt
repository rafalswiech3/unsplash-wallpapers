package com.rafal.unsplashwallpapers.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rafal.unsplashwallpapers.model.UnsplashApi
import com.rafal.unsplashwallpapers.model.UnsplashPhoto
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException

class UnsplashPhotoPagingSource(
    private val api: UnsplashApi,
    private val query: String
    ): PagingSource<Int, UnsplashPhoto>() {

    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val position = params.key ?: 1

        return try {
            val response = api.searchPhotos(query, position).awaitResponse()
            val results = response.body()!!.results
            LoadResult.Page(
                data = results,
                prevKey = if(position == 1) null else position - 1,
                nextKey = if(results.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}