package com.dhairytripathi.markettoday.data.storage

import androidx.room.Database
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dhairytripathi.markettoday.data.storage.entity.NewsArticleDb

@Database(
    entities = [NewsArticleDb::class],
    version = NewsDatabaseMigration.latestVersion
)
abstract class NewsDatabase : RoomDatabase() {

    /**
     * Get news article DAO
     */
    abstract fun newsArticlesDao(): NewsArticlesDao

    companion object {

        private const val databaseName = "news-db"

        fun buildDefault(context: Context) =
            Room.databaseBuilder(context, NewsDatabase::class.java, databaseName)
                .addMigrations(*NewsDatabaseMigration.allMigrations)
                .build()

        @VisibleForTesting
        fun buildTest(context: Context) =
            Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java)
                .build()
    }
}