package com.example.mtgdeckbuilder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Database(entities = [Deck::class, DatabaseCard::class, DeckCardCrossRef::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
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

class Converters {
    @TypeConverter
    fun fromArray(array: Array<String>): String {
        return Json.encodeToString(array)
    }
    @TypeConverter
    fun toArray(jsonString: String): Array<String> {
        return Json.decodeFromString(jsonString)
    }
}