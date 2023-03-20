package mn.openlocations.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class StoredServer : RealmObject {
    @PrimaryKey
    var id: String = ObjectId().toHexString()
    var address: String = ""
    var name: String = ""
    var latitude: Double = .0
    var longitude: Double = .0
}
