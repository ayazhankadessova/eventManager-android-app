//package com.example.eventmanager
//
//
//import android.content.Context
//import androidx.lifecycle.LiveData
//import androidx.room.*
////It's not hard to understand the overall concept. Each function (getAll, getByDeptId, etc.) queries the database. So in total there are three functions.
////Standard queries like insert, update, delete can use the query templates @Insert, @Delete and @Update.
////For queries that need more complex logic, we use @Query, which accepts an SQL statement.
//
////Typically, the UI and main logic run in the main thread, while IO-intensive operations like database or network queries run in IO coroutines.
//@Dao
//interface EventDatabaseDao {
//    @Query("SELECT * from event")
//    fun getAll(): LiveData<List<Event>>
//
//    @Query("SELECT * from event where highlight = true")
//    fun getHighlight(id: String): LiveData<List<Event>>
//
////    @Query("SELECT * from event where =true")
////    fun getSaved(): LiveData<List<Event>>
//
//    @Update
//    suspend fun update(event:Event)
//
//    @Delete
//    suspend fun delete(event: Event)
//}
//
//@Database(entities = [Event::class], version = 1)
//abstract class EventDatabase : RoomDatabase() {
//    abstract fun eventDao(): EventDatabaseDao
//
//    companion object {
//        private var INSTANCE: EventDatabase? = null
//        fun getInstance(context: Context): EventDatabase {
//            synchronized(this) {
//                var instance = INSTANCE
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        EventDatabase::class.java,
//                        "event_database"
//                    )
//                        .createFromAsset("events.db")
//                        .fallbackToDestructiveMigration()
//                        .build()
//                    INSTANCE = instance
//                }
//                return instance
//            }
//        }
//    }
//}