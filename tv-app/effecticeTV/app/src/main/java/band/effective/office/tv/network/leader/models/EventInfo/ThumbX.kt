package band.effective.office.tv.network.leader.models.EventInfo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ThumbX(
    val `180`: String,
    val `360`: String,
    val `520`: String
)