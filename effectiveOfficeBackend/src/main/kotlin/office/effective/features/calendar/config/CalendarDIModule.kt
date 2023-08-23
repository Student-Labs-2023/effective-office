package office.effective.features.calendar.config

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import office.effective.features.calendar.repository.CalendarRepository
import org.koin.dsl.module
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import office.effective.config
import java.io.ByteArrayInputStream

val calendarDiModule = module(createdAtStart = true) {
    single { CalendarRepository(get(), get(), get()) }
    single<JsonFactory> { GsonFactory.getDefaultInstance() }
    single<HttpTransport> { GoogleNetHttpTransport.newTrustedTransport() }
    single<GoogleCredentials> { getCredential(get()) }
    single<Calendar> {
        Calendar.Builder(get(), get(), HttpCredentialsAdapter(get<GoogleCredentials>())).setApplicationName("APPLICATION_NAME").build()
    }
}

private fun getCredential(httpTransport: HttpTransport): GoogleCredentials {
    val authorGoogleAccount: String = config.propertyOrNull("auth.app.defaultAppEmail")?.getString() ?: throw Exception(
        "Config file does not contain default gmail value"
    )
    val inputStream: ByteArrayInputStream = ByteArrayInputStream(System.getenv("JSON_CREDENTIALS").toByteArray())
    return GoogleCredentials.fromStream(inputStream).createScoped(CalendarScopes.CALENDAR)
        .createDelegated(authorGoogleAccount)
}
