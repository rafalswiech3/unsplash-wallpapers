package com.rafal.unsplashwallpapers.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafal.unsplashwallpapers.model.db.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE repoId = :repoId")
    suspend fun remoteKeysRepoId(repoId: String): RemoteKeys?

    @Query("SELECT * FROM remote_keys")
    fun getAll() : List<RemoteKeys>

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}