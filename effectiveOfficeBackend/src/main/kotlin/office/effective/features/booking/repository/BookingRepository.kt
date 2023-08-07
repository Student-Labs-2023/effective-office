package office.effective.features.booking.repository

import office.effective.common.exception.InstanceNotFoundException
import office.effective.common.exception.MissingIdException
import office.effective.features.booking.converters.BookingRepositoryConverter
import office.effective.features.user.repository.*
import office.effective.model.Booking
import office.effective.model.UserModel
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import java.util.*
import kotlin.collections.List

class BookingRepository(private val database: Database, private val converter: BookingRepositoryConverter) {

    /**
     * Returns whether a booking with the given id exists
     *
     * @author Daniil Zavyalov
     */
    fun existsById(id: UUID): Boolean {
        return database.workspaceBooking.count { it.id eq id } > 0
    }

    /**
     * Deletes the booking with the given id
     *
     * If the booking is not found in the database it is silently ignored
     *
     * @author Daniil Zavyalov
     */
    fun deleteById(id: UUID) {
        database.bookingParticipants.removeIf { it.bookingId eq id }
        database.workspaceBooking.removeIf { it.id eq id }
    }

    /**
     * Retrieves a booking model by its id.
     * Retrieved booking contains user and workspace models without integrations and utilities
     *
     * @author Daniil Zavyalov
     */
    fun findById(bookingId: UUID): Booking? {
        val entity = database.workspaceBooking.find { it.id eq bookingId } ?: return null
        val participants = findParticipants(bookingId)
        return entity.let { converter.entityToModel(it, participants) }
    }

    /**
     * Returns all bookings with the given owner id
     *
     * @author Daniil Zavyalov
     */
    fun findAllByOwnerId(ownerId: UUID): List<Booking> {
        val entityList = database.workspaceBooking.filter { it.ownerId eq ownerId }.toList()
        return entityList.map {
            val participants = findParticipants(it.id)
            converter.entityToModel(it, participants)
        }
    }

    /**
     * Returns all bookings with the given workspace id
     *
     * @author Daniil Zavyalov
     */
    fun findAllByWorkspaceId(workspaceId: UUID): List<Booking> {
        val entityList = database.workspaceBooking.filter { it.workspaceId eq workspaceId }.toList()
        return entityList.map {
            val participants = findParticipants(it.id)
            converter.entityToModel(it, participants)
        }
    }

    /**
     * Returns all bookings with the given workspace and owner id
     *
     * @author Daniil Zavyalov
     */
    fun findAllByOwnerAndWorkspaceId(ownerId: UUID, workspaceId: UUID): List<Booking> {
        val entityList = database.workspaceBooking
            .filter { it.ownerId eq ownerId }
            .filter { it.workspaceId eq workspaceId }.toList()

        return entityList.map {
            val participants = findParticipants(it.id)
            converter.entityToModel(it, participants)
        }
    }

    /**
     * Returns all bookings
     *
     * @author Daniil Zavyalov
     */
    fun findAll(): List<Booking> {
        val entityList = database.workspaceBooking.toList()
        return entityList.map {
            val participants = findParticipants(it.id)
            converter.entityToModel(it, participants)
        }
    }

    /**
     * Retrieves all users who participate in the booking with the given id
     *
     * @author Daniil Zavyalov
     */
    private fun findParticipants(bookingId: UUID): List<UserEntity> {
        return database
            .from(BookingParticipants)
            .innerJoin(right = Users, on = BookingParticipants.userId eq Users.id)
            .select()
            .where { BookingParticipants.bookingId eq bookingId }
            .map { row -> Users.createEntity(row) }
    }

    /**
     * Saves a given booking. If given model will have an id, it will be ignored.
     * Use the returned model for further operations
     *
     * @author Daniil Zavyalov
     */
    fun save(booking: Booking): Booking {
        val id = UUID.randomUUID()
        booking.id = id
        val entity = converter.modelToEntity(booking)

        database.workspaceBooking.add(converter.modelToEntity(booking))

        val participantList = findParticipantEntities(booking.participants)
        for(participant in participantList) {
            database.insert(BookingParticipants) {
                set(it.bookingId, entity.id)
                set(it.userId, participant.id)
            }
        }
        return booking
    }

    /**
     * Updates a given booking. Use the returned model for further operations
     *
     * Throws InstanceNotFoundException if booking given id doesn't exist in the database
     *
     * @author Daniil Zavyalov
     */
    fun update(booking: Booking): Booking {
        booking.id?.let {
            if(!existsById(it))
                throw InstanceNotFoundException(WorkspaceBookingEntity::class, "Booking with id $it not wound")
        }

        val entity = converter.modelToEntity(booking)
        database.workspaceBooking.update(entity)

        database.bookingParticipants.removeIf { it.bookingId eq entity.id }
        saveParticipants(booking.participants, entity.id)
        return booking
    }

    /**
     * Adds many-to-many relationship between booking and its participants
     *
     * @author Daniil Zavyalov
     */
    private fun saveParticipants(participantModels: List<UserModel>, bookingId: UUID): List<UserEntity> {
        val participantList = findParticipantEntities(participantModels)
        for(participant in participantList) {
            database.insert(BookingParticipants) {
                set(it.bookingId, bookingId)
                set(it.userId, participant.id)
            }
        }
        return participantList
    }

    /**
     * Retrieves user entities by the ids of the given user models
     *
     * @author Daniil Zavyalov
     */
    private fun findParticipantEntities(participantModels: List<UserModel>): List<UserEntity> {
        val participantList = mutableListOf<UserEntity>()

        for (participant in participantModels) {
            val participantId: UUID = participant.id
                ?: throw MissingIdException("User with name ${ participant.fullName } doesn't have an id")

            val user: UserEntity = database.users.find { it.id eq participantId }
                ?: throw InstanceNotFoundException(UserEntity::class, "User with id $participantId not found", participantId)
            participantList.add(user)
        }
        return participantList
    }
}