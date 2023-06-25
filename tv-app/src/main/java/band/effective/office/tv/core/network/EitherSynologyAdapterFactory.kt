package band.effective.office.tv.core.network

import android.util.Log
import band.effective.office.tv.core.network.entity.Either
import band.effective.office.tv.core.network.entity.ErrorReason
import band.effective.office.tv.network.synology.models.error.SynologyApiError
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

internal class EitherSynologyAdapterFactory @Inject constructor() : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<out Any, Call<Either<ErrorReason, *>>>? {
        if (returnType !is ParameterizedType) {
            return null
        }

        val containerType = getParameterUpperBound(0, returnType)

        if (getRawType(containerType) != Either::class.java)
            return null

        if (containerType !is ParameterizedType) {
            return null
        }

        val errorType = getParameterUpperBound(0, containerType)
        if (getRawType(errorType) != ErrorReason::class.java)
            return null

        val errorBodyConverter = retrofit.responseBodyConverter<SynologyApiError>(
            SynologyApiError::class.java,
            annotations
        )

        val resultType = getParameterUpperBound(1, containerType)
        return ResultCallAdapter(
            resultType,
            errorBodyConverter
        )
    }

    private class ResultCallAdapter<R, T>(
        private val resultType: Type,
        private val errorBodyConverter: Converter<ResponseBody, SynologyApiError>
    ) :
        CallAdapter<R, Call<Either<ErrorReason, T>>> {

        override fun adapt(call: Call<R>): Call<Either<ErrorReason, T>> =
            ResultCallWrapper(
                call,
                errorBodyConverter
            )

        override fun responseType() = resultType
    }

    private class ResultCallWrapper<F, T>(
        private val delegate: Call<T>,
        private val errorBodyConverter: Converter<ResponseBody, SynologyApiError>
    ) : Call<Either<ErrorReason, F>> {

        override fun execute() = wrapResponse(delegate.execute())

        override fun enqueue(callback: Callback<Either<ErrorReason, F>>) {
            try {
                delegate.enqueue(object : Callback<T> {
                    override fun onResponse(call: Call<T>, response: Response<T>) {
                        callback.onResponse(this@ResultCallWrapper, wrapResponse(response))
                    }

                    override fun onFailure(call: Call<T>, t: Throwable) {
                        Log.e("NetworkError", t.message.toString())
                        val errorReason = when (t) {
                            is UnknownHostException -> ErrorReason.NetworkError(t)
                            is TimeoutException -> ErrorReason.NetworkError(t)
                            else -> ErrorReason.UnexpectedError(t)
                        }
                        val either = Either.Failure(errorReason)
                        val failedResponse = Response.success(either as Either<ErrorReason, F>)
                        callback.onResponse(this@ResultCallWrapper, failedResponse)
                    }
                })
            } catch (e: Exception) {
                Log.e("NetworkError", e.message.toString())
                val unexpectedErrorResponse = Response.success(
                    Either.Failure(ErrorReason.UnexpectedError(e)) as Either<ErrorReason, F>
                )
                callback.onResponse(this@ResultCallWrapper, unexpectedErrorResponse)
            }
        }

        override fun isExecuted() = delegate.isExecuted

        override fun isCanceled() = delegate.isCanceled

        override fun cancel() = delegate.cancel()

        override fun request(): Request = delegate.request()

        override fun timeout(): Timeout = delegate.timeout()

        override fun clone() = ResultCallWrapper<F, T>(
            delegate.clone(),
            errorBodyConverter
        )

        @Suppress("UNCHECKED_CAST")
        private fun wrapResponse(response: Response<T>): Response<Either<ErrorReason, F>> {
            return try {
                if (response.isSuccessful) {
                    Response.success(Either.Success(response.body()) as Either<ErrorReason, F>)
                } else {
                    val errorResponse =
                        errorBodyConverter.convert(response.errorBody() as ResponseBody)
                            ?: error("Invalid format for error response")
                    val reason = getReasonByCode(response, errorResponse)
                    Response.success(Either.Failure(reason))
                }
            } catch (e: Exception) {
                Log.e("NetworkError", e.message.toString())
                Response.success(Either.Failure(ErrorReason.UnexpectedError(e)) as Either<ErrorReason, F>)
            }
        }

        private fun getReasonByCode(
            response: Response<T>,
            errorResponse: SynologyApiError
        ) = when (errorResponse.error.code) {
            404 -> ErrorReason.NotFound("Not found")
            119 -> ErrorReason.ServerError("Authorization error")
            else -> ErrorReason.ServerError("Error with synology api. Error code ${errorResponse.error.code}")
        }
    }
}
