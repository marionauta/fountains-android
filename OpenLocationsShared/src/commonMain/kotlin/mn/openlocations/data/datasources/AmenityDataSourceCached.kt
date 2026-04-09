package mn.openlocations.data.datasources

import mn.openlocations.data.models.OverpassNw

internal interface AmenityDataSourceCached: AmenityDataSource {
    fun save(amenities: Collection<OverpassNw>)
}
