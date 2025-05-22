import kotlinx.serialization.Serializable

@Serializable
data class ItemData(val data: Map<String, String>)

@Serializable
data class ItemWrapper(val Items: List<Map<String, String>>)