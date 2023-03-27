package mn.openlocations.domain.models

data class Area(
    val id: String,
    val name: String,
    val location: Location,
    val osmAreaId: Long,
)
