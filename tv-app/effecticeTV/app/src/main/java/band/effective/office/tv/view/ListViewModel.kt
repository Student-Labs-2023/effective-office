package band.effective.office.tv.view

import band.effective.office.tv.leader.models.Photo
import band.effective.office.tv.leader.LeaderApi
import band.effective.office.tv.leader.PhotoJsonDeserializer
import band.effective.office.tv.leader.SearchResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListViewModel: ViewModel() {
    private var mutableApiResponse = MutableStateFlow<SearchResponse?>((null))
    val apiResponse = mutableApiResponse.asStateFlow()
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val builder = GsonBuilder()
            builder.registerTypeAdapter(Photo::class.java, PhotoJsonDeserializer())
            val retrofit = Retrofit.Builder()
                .baseUrl("https://leader-id.ru/")
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .build();
            val api = retrofit.create(LeaderApi::class.java)
            mutableApiResponse.update { api.searchEvents(10,3942).execute().body() }
        }
    }
}