package band.effective.office.elevator.expects

import band.effective.office.elevator.ui.uiViewController
import io.github.aakira.napier.Napier
import platform.UIKit.UIAlertController
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.Foundation.*
import platform.UIKit.*

actual fun showToast(message: String) {
    Napier.e { message }
    val controller = UIAlertController(null, null)
    controller.message = message
    controller.showViewController(uiViewController, null)
}

actual fun generateVibration(milliseconds: Long) {
//    AudioServicesPlaySystemSound(kSystemSoundID_Vibrate)
}

actual fun makeCall(phoneNumber: String) {
    val url = NSURL(string = "tel:$phoneNumber")
    UIApplication.sharedApplication.openURL(url)
}

actual fun pickTelegram(telegramNick: String) {
    val urlString = "https://telegram.me/$telegramNick"
    val url = NSURL.URLWithString(urlString)
    val application = UIApplication.sharedApplication
    application.openURL(url)
}

actual fun pickSBP(phoneNumber: String){
    try {
        val app = UIApplication.sharedApplication
        val url = NSURL(string = "sberbankmobile://")
        app.openURL(url)
    } catch (e: Exception) {
        val url = NSURL(string = "https://web4-new.online.sberbank.ru/payments/fps")
        UIApplication.sharedApplication.openURL(url)
    }
}
