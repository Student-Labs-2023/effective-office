package band.effective.office.tv.screen.photo.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.tv.foundation.lazy.list.TvLazyListState
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.foundation.lazy.list.rememberTvLazyListState

import band.effective.office.tv.screen.photo.model.Photo
import okhttp3.OkHttpClient

@Composable
fun PhotoSlideShow(
    photos: List<Photo>,
    lazyListState: TvLazyListState = rememberTvLazyListState(),
    modifier: Modifier = Modifier,
    modifierForFocus: Modifier = Modifier,
    unsafeOkHttpClient: OkHttpClient
) {
    TvLazyRow(
        state = lazyListState,
        modifier = modifier
            .fillMaxSize()
            .focusable()
    ) {
        items(photos) { photo ->
            PhotoUIItem(
                image = photo,
                modifier = modifierForFocus
                    .fillMaxSize(),
                okHttpClient = unsafeOkHttpClient
            )
        }
    }
}