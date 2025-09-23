package com.example.flightsearch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flightsearch.data.airport.Airport
import com.example.flightsearch.data.airport.AirportDao
import com.example.flightsearch.data.favorite.Favorite
import com.example.flightsearch.data.favorite.FavoriteDao

@Database(entities = [Airport::class, Favorite::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun airportDao(): AirportDao

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_NAME = "app_database"

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                val dbFile = context.getDatabasePath(DATABASE_NAME)
                Room
                    .databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "app_database",
                    )
                    .apply {
                        if (!dbFile.exists()) {
                            createFromAsset("database/flight_search.db")
                        }
                    }
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
    }
}
