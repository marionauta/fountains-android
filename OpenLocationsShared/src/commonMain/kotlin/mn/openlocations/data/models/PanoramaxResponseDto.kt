package mn.openlocations.data.models

import kotlinx.serialization.Serializable

@Serializable
internal class PanoramaxResponseDto(val features: List<Feature>)

@Serializable
internal class Feature(
    val assets: Assets,
    val links: List<Link>,
    val providers: List<Provider>,
    val properties: Properties,
)

@Serializable
internal class Assets(
    val thumb: Asset,
)

@Serializable
internal class Asset(
    val href: String,
)

@Serializable
internal class Link(
    val rel: String,
    val href: String,
)

@Serializable
internal class Properties(
    val license: String,
)

@Serializable
internal class Provider(
    val name: String,
    val roles: List<String>,
)
