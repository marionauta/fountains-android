package mn.openlocations.data.datasources

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mn.openlocations.data.models.StoredArea

class StoredAreasDataSource {
    private val config = RealmConfiguration.create(schema = setOf(StoredArea::class))
    private val realm = Realm.open(config)

    suspend fun get(id: String): StoredArea? {
        return realm.query<StoredArea>("id == $0", id).first().find()
    }

    suspend fun all(): List<StoredArea> {
        return realm.query<StoredArea>().find()
    }

    fun allStream(): Flow<List<StoredArea>> {
        return realm.query<StoredArea>().asFlow().map { it.list.toList() }
    }

    suspend fun add(area: StoredArea) {
        realm.write {
            copyToRealm(area)
        }
    }

    suspend fun delete(id: String) {
        realm.write {
            val results = query<StoredArea>("id == $0", id).find()
            delete(results)
        }
    }
}
