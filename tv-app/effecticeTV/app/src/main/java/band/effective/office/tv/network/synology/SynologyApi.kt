package band.effective.office.tv.network.synology

import band.effective.office.tv.network.synology.response.SynologyAuthResponse
import band.effective.office.tv.network.synology.response.SynologyListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SynologyApi {
    @GET("/webapi/auth.cgi?api=SYNO.API.Auth&session=FileStation&format=sid")
    suspend fun auth(
        @Query("version") version: Int,
        @Query("method") method: String,
        @Query("account") login: String,
        @Query("passwd") password: String
        ): Response<SynologyAuthResponse>

    @GET("/webapi/entry.cgi?api=SYNO.FileStation.List")
    suspend fun getFiles(
        @Query("_sid") sid: String,
        @Query("version") version: Int,
        @Query("method") method: String,
        @Query("folder_path") folderPath: String
    ): Response<SynologyListResponse>
}