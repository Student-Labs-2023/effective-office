package band.effective.office.elevator.common.compose

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource

@Composable
internal actual fun imageResource(id: String): ImageBitmap {
    val context = Android.context
    val id = context.resources.getIdentifier(id, "drawable", context.packageName)
    return ImageBitmap.Companion.imageResource(id = id)
}

object Android {
    lateinit var context: Context
    lateinit var activity: Activity
}