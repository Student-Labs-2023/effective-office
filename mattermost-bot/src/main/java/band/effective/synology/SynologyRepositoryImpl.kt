package band.effective.synology

import band.effective.SynologySettings
import band.effective.core.Either
import band.effective.core.ErrorReason
import band.effective.core.moshi
import band.effective.core.synologyApi
import band.effective.synology.models.SynologyAlbumInfo
import band.effective.synology.models.respone.AddPhotoToAlbumResponse
import band.effective.synology.models.respone.SynologyAlbumsResponse
import band.effective.synology.models.respone.UploadPhotoResponse
import band.effective.utils.getEnv
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.util.*
import java.util.zip.GZIPInputStream

class SynologyRepositoryImpl : SynologyRepository {

    private var cookie: String? = null

    private var currentAlbumId: Int? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            login()
        }
    }

    private suspend fun login() {
        println("login in synology")
        val res = synologyApi.auth(
                version = 3,
                method = "login",
                login = getEnv(SynologySettings.synologyAccount),
                password = getEnv(SynologySettings.synologyPassword),
        )
        cookie = ""
        val headersCookie = res.headers().toMultimap()["Set-Cookie"]
        cookie = headersCookie?.reduce { first, second -> first + second }
    }

    private suspend fun getAlbums(): Either<ErrorReason, SynologyAlbumsResponse> =
            synologyApi.getAlbums(
                    cookie = cookie.orEmpty(),
                    version = 2,
                    method = "list",
                    offset = 0,
                    limit = 100
            )

    private suspend fun uploadPhoto(
            requestBody: RequestBody
    ): Either<ErrorReason, UploadPhotoResponse> {
        return synologyApi.uploadPhotoEither(body = requestBody, cookie = cookie.orEmpty())
    }

    private suspend fun addPhotoToAlbums(
            albumId: Int,
            itemId: Int
    ): Either<ErrorReason, AddPhotoToAlbumResponse> {
        val requestBody = RequestBody.create(
                MediaType.parse("text/plane"),
                "api=SYNO.Foto.Browse.NormalAlbum&method=add_item&version=1&item=%5B$itemId%5D&id=$albumId"
        )
        return synologyApi.addPhotoToAlbum(request = requestBody, cookie = cookie.orEmpty())
    }

    override suspend fun uploadPhotoToAlbum(
            file: ByteArray,
            fileName: String,
            fileType: String
    ): Either<ErrorReason, UploadPhotoResponse> {
        if (cookie == null) login()

        if (currentAlbumId == null) {
            when (val albumsReq = getAlbums()) {
                is Either.Success -> {
                    println("get album success")
                    val albums = albumsReq.data.albumsData.albums
                    val currentAlbumName = currentAlbumName()
                    albums.find { album ->
                        album.name == currentAlbumName
                    }.let { album ->
                        currentAlbumId = if (album != null) album.id
                        else when (val createAlbum = createAlbum(currentAlbumName)) {
                            is Either.Success -> createAlbum.data.albumId
                            is Either.Failure -> throw Error("albums $currentAlbumName not found and cant be created ")
                        }
                    }
                }

                is Either.Failure -> {
                    return Either.Failure(albumsReq.error)
                }
            }
        }
        val requestBody = setRequestToUpload(file, fileName, fileType)
        return uploadPhoto(requestBody)
    }

    private fun setRequestToUpload(file: ByteArray, fileName: String, fileType: String): RequestBody {

        val reqApi = RequestBody.create(MediaType.parse("text/plain"), "SYNO.Foto.Upload.Item")
        val reqMethod = RequestBody.create(MediaType.parse("text/plain"), "upload")
        val reqVersion = RequestBody.create(MediaType.parse("text/plain"), "1")
        val reqDuplicate = RequestBody.create(MediaType.parse("text/plain"), "\"ignore\"")
        val reqName = RequestBody.create(MediaType.parse("text/plain"), "\"$fileName\"")
        val reqAlbumId = RequestBody.create(MediaType.parse("text/plain"), (currentAlbumId ?: 0).toString())

        return MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("api", null, removeHeaderFromRequestBody(reqApi))
                .addFormDataPart("method", null, removeHeaderFromRequestBody(reqMethod))
                .addFormDataPart("version", null, removeHeaderFromRequestBody(reqVersion))
                .addFormDataPart("file", fileName, removeHeaderFromRequestBody(RequestBody.create(
                        MediaType.get(fileType),
                        file
                )))
                .addFormDataPart("duplicate", null, removeHeaderFromRequestBody(reqDuplicate))
                .addFormDataPart("name", null, removeHeaderFromRequestBody(reqName))
                .addFormDataPart("album_id", null, removeHeaderFromRequestBody(reqAlbumId))
                .build()
    }


    private suspend fun createAlbum(albumName: String): Either<ErrorReason, SynologyAlbumInfo> {
        val requestBody = RequestBody.create(MediaType.get("text/plane"), "api=SYNO.Foto.Browse.NormalAlbum&method=create&version=1&name=%22$albumName%22&item=%5B%5D")
        return when (val album = synologyApi.createAlbum(cookie = cookie.orEmpty(), requestBody)) {
            is Either.Success -> {
                Either.Success(SynologyAlbumInfo(album.data.albumsData.album.id))
            }

            is Either.Failure -> {
                Either.Failure(album.error)
            }
        }
    }

    private fun currentAlbumName(): String {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return "${getEnv(SynologySettings.synologyAlbumTypeName)} $currentYear"
    }

    // This method remove headers from multipart, because synology photo don't work with these headers
    private fun removeHeaderFromRequestBody(delegate: RequestBody): RequestBody {
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return null
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                delegate.writeTo(sink)
            }
        }
    }
}