package com.example.mtgdeckbuilder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [Deck::class], version = 1, exportSchema = false)
abstract class DeckDatabase: RoomDatabase() {

    abstract fun deckDao(): DeckDao

    companion object {
        @Volatile
        private var Instance: DeckDatabase? = null

        fun getDatabase(context: Context): DeckDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, DeckDatabase::class.java,"deckList")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}