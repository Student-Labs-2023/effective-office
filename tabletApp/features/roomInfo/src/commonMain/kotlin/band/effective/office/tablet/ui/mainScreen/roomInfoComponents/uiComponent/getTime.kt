package band.effective.office.tablet.ui.mainScreen.roomInfoComponents.uiComponent

import band.effective.office.tablet.features.roomInfo.MainRes
import band.effective.office.tablet.utils.getCorrectDeclension

internal fun Int.getTime(): String {
    val min = this % 60000
    val hours = this / 60000
    val minStr = "$min ${
        getCorrectDeclension(
            number = min,
            nominativeCase = MainRes.string.minuit_nominative,
            genitive = MainRes.string.minuit_genitive,
            genitivePlural = MainRes.string.minuit_plural
        )
    }"
    val hourStr = "$min ${
        getCorrectDeclension(
            number = min,
            nominativeCase = MainRes.string.hour_nominative,
            genitive = MainRes.string.hour_genitive,
            genitivePlural = MainRes.string.hour_plural
        )
    }"
    return when {
        hours == 0 -> minStr
        min == 0 -> hourStr
        else -> "$hourStr $minStr"
    }
}