package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

internal interface ScreenLifecycleOwner {
    @Composable
    public fun ProvideBeforeScreenContent(
        provideSaveableState: @Composable (suffixKey: String, content: @Composable () -> Unit) -> Unit,
        content: @Composable () -> Unit
    ): Unit = content()

     fun onDispose(screen: Screen) {}
}

internal object DefaultScreenLifecycleOwner : ScreenLifecycleOwner
