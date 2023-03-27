package mn.openlocations.data.datasources

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mn.openlocations.data.models.StoredServer

class StoredServersDataSource {
    private val config = RealmConfiguration.create(schema = setOf(StoredServer::class))
    private val realm = Realm.open(config)

    suspend fun get(id: String): StoredServer? {
        return realm.query<StoredServer>("id == $0", id).first().find()
    }

    suspend fun all(): List<StoredServer> {
        return realm.query<StoredServer>().find()
    }

    fun allStream(): Flow<List<StoredServer>> {
        return realm.query<StoredServer>().asFlow().map { it.list.toList() }
    }

    suspend fun add(server: StoredServer) {
        realm.write {
            copyToRealm(server)
        }
    }

    suspend fun delete(id: String) {
        realm.write {
            val results = query<StoredServer>("id == $0", id).find().first()
            delete(results)
        }
    }
}
