package band.effective.office.elevator.expects

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import android.content.Intent
import android.net.Uri
import band.effective.office.elevator.AndroidApp

actual fun showToast(message: String) {
    Toast.makeText(AndroidApp.INSTANCE.applicationContext, message, Toast.LENGTH_SHORT).show()
}

actual fun generateVibration(milliseconds: Long) {
    val vibrator =
        AndroidApp.INSTANCE.applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                milliseconds,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    } else {
        vibrator.vibrate(milliseconds)
    }
}

actual fun makeCall(phoneNumber: String) {
    with(AndroidApp.INSTANCE.applicationContext) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        this@with.startActivity(intent)
    }
}

actual fun pickTelegram(telegramNick: String) {
    with(AndroidApp.INSTANCE.applicationContext) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://t.me/$telegramNick")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            `package` = "org.telegram.messenger"
        }
        this@with.startActivity(intent)
    }
}

@SuppressLint("WrongConstant")
actual fun pickSBP(phoneNumber: String) {
    with(AndroidApp.INSTANCE.applicationContext) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("tel:$phoneNumber")
            flags = 0x24000000 or Intent.FLAG_ACTIVITY_NEW_TASK
//            component = ComponentName(
//                "ru.sberbankmobile",
//                "ru.sberbank.mobile.auth.presentation.splash.SplashActivity"
//            )
        }
        this@with.startActivity(intent)
    }
}