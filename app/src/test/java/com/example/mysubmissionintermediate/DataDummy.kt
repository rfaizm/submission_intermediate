package com.example.mysubmissionintermediate

import com.example.mysubmissionintermediate.data.local.entity.StoryLocal

object DataDummy {

    fun generateDummyStoryResponse(): List<StoryLocal> {
        val items: MutableList<StoryLocal> = arrayListOf()
        for (i in 0..100) {
            val quote = StoryLocal(
                i.toString(),
                "author + $i",
                "quote $i",
                "created at $i",
                "photo $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(quote)
        }
        return items
    }
}