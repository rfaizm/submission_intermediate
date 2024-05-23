package com.example.mysubmissionintermediate.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mysubmissionintermediate.data.local.entity.StoryLocal

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(story: List<StoryLocal>)

    @Query("SELECT * FROM story")
    fun getStories(): PagingSource<Int, StoryLocal>


    @Query("DELETE FROM story")
    fun deleteAll()
}