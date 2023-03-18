package band.effective.office.tv.network.core

sealed class ErrorReason {
    abstract val message: String

    class NotFound(
        override val message: String
    ) : ErrorReason()

    class ServerError(
        override val message: String
    ) : ErrorReason()

    data class NetworkError(val throwable: Throwable) : ErrorReason() {
        override val message: String = throwable.localizedMessage.orEmpty()
    }

    data class UnexpectedError(val throwable: Throwable) : ErrorReason() {
        override val message: String = throwable.localizedMessage.orEmpty()
    }
}