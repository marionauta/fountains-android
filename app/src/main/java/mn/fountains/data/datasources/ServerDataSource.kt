package mn.fountains.data.datasources

import android.content.Context
import androidx.room.*
import mn.fountains.data.models.ServerEntity
import java.net.URL

class ServerDataSource {
    suspend fun get(context: Context, address: String): ServerEntity? {
        val db = AppDatabase.getInstance(context)
        return db.serverDao().get(address)
    }

    suspend fun all(context: Context): List<ServerEntity> {
        val db = AppDatabase.getInstance(context)
        return db.serverDao().getAll()
    }

    suspend fun add(context: Context, server: ServerEntity) {
        val db = AppDatabase.getInstance(context)
        return db.serverDao().add(server)
    }
}

@Dao
interface ServerDao {
    @Query("SELECT * FROM servers")
    suspend fun getAll(): List<ServerEntity>

    @Query("SELECT * FROM servers WHERE address = :address LIMIT 1")
    suspend fun get(address: String): ServerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(server: ServerEntity)
}

@Database(entities = [ServerEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun serverDao(): ServerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database",
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
