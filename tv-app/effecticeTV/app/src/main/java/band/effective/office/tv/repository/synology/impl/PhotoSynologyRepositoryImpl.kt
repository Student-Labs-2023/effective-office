package band.effective.office.tv.repository.synology.impl

import band.effective.office.tv.core.network.entity.Either
import band.effective.office.tv.core.network.entity.ErrorReason
import band.effective.office.tv.domain.model.synology.PhotoDomain
import band.effective.office.tv.domain.model.synology.toDomain
import band.effective.office.tv.network.synology.SynologyApi
import band.effective.office.tv.network.synology.models.response.SynologyAlbumsResponse
import band.effective.office.tv.repository.synology.PhotoSynologyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PhotoSynologyRepositoryImpl @Inject constructor(
    private val synologyApi: SynologyApi,
) : PhotoSynologyRepository {
    override suspend fun getPhotosUrl(sid: String): Flow<Either<String, List<PhotoDomain>>> =
        flow {
            val photos: MutableList<PhotoDomain> = mutableListOf()
            when (
                val res: Either<ErrorReason, SynologyAlbumsResponse> =
                    synologyApi.getAlbums(
                        sid = sid,
                        version = 2,
                        method = "list",
                        offset = 0,
                        limit = 100
                    )
            ) {
                is Either.Success -> {
                    res.data.albumsData.albums.filter { it.name.contains("Best Photo") }.forEach { album ->
                        when (
                            val photo = synologyApi.getPhotosFromAlbum(
                                sid = sid,
                                version = 1,
                                method = "list",
                                albumId = album.id,
                                offset = 0,
                                limit = album.item_count
                            )
                        ) {
                            is Either.Success ->
                                photos.addAll(photo.data.toDomain(sid = sid))
                            else -> {}
                        }
                    }
                    emit(Either.Success(photos))
                }
                is Either.Failure -> {
                    emit(Either.Failure(res.error.message))
                }
            }
        }
}