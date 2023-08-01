package office.effective.features.booking.repository

import office.effective.features.booking.converters.BookingRepositoryConverter
import office.effective.features.user.repository.*
import office.effective.model.Booking
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.removeIf
import org.ktorm.entity.toList
import java.util.*
import kotlin.collections.List

class BookingRepository(private val database: Database, private val converter: BookingRepositoryConverter) {

    fun findById(bookingId: UUID): Booking? {
        val entity = database.workspaceBooking.find { it.id eq bookingId } ?: return null
        val participants = findParticipants(bookingId)
        return entity.let { converter.entityToModel(it, participants) }
    }

    private fun findParticipants(bookingId: UUID): List<UserEntity> {
        return database
            .from(BookingParticipants)
            .innerJoin(right = Users, on = BookingParticipants.userId eq Users.id)
            .select()
            .where { BookingParticipants.bookingId eq bookingId }
            .map { row -> Users.createEntity(row) }
    }

    fun findAllByOwnerId(ownerId: UUID): List<Booking> {
        val entityList = database.workspaceBooking.filter { it.ownerId eq ownerId }.toList()
        return entityList.map {
            val participants = findParticipants(it.id)
            converter.entityToModel(it, participants)
        }
    }

    fun deleteById(id: UUID) {
        database.workspaceBooking.removeIf { it.id eq id }
    }
}