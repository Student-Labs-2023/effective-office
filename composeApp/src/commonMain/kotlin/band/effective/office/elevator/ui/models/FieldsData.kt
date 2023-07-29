package band.effective.office.elevator.ui.models

import androidx.compose.runtime.MutableState
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource

data class FieldsData(
    val title:StringResource,
    val icon:ImageResource,
    val value: MutableState<String>
    )