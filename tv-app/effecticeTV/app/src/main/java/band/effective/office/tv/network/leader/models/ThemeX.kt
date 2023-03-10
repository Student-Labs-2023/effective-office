package band.effective.office.tv.network.leader.models

import com.squareup.moshi.Json

data class ThemeX(
    val childCount: Int,
    val code: Any,
    val createdBy: Int,
    val gid: Int,
    val id: Int,
    val keywords: List<String>,
    val moderatedAt: Any,
    val moderatedBy: Any,
    val name: String,
    @Json(name = "old_type")
    val oldType: String,
    val parentId: Int,
    val photo: PhotoXXX,
    val photos: Photos,
    val priority: Int,
    val status: Int,
    @Json(name = "updated_at")
    val updatedAt: String,
    val visible: Boolean
)