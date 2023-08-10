package office.effective.features.user.repository

import office.effective.common.exception.*
import office.effective.features.user.converters.IntegrationModelEntityConverter
import office.effective.features.user.converters.UserModelEntityConverter
import office.effective.model.IntegrationModel
import office.effective.model.UserModel
import office.effective.model.UserTagModel
import org.koin.core.context.GlobalContext
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import java.util.*

class UserRepository(private val db: Database, private val converter: UserModelEntityConverter) {

    /**
     * Checks existence of user by id, using count
     * @return Boolean (true - exists)
     *
     * @author Danil Kiselev
     * */
    fun existsById(userId: UUID): Boolean {
        return db.users.count { it.id eq userId } > 0
    }

    /**
     * @return UserModel if user exists
     * @throws InstanceNotFoundException if user not found
     *
     * @author Danil Kiselev
     * */
    fun findById(userId: UUID): UserModel {
        val userEnt: UserEntity =
            db.users.find { it.id eq userId } ?: throw InstanceNotFoundException(UserEntity::class, "User ${userId}")
        val integrations = findSetOfIntegrationsByUser(userId)
        val tagEntity = db.users_tags.find { it.id eq userEnt.tag.id } ?: throw InstanceNotFoundException(
            UsersTagEntity::class, "Cannot find tag by id ${userEnt.tag.id}"
        )

        val userModel = converter.entityToModel(userEnt, null)
        userModel.integrations = integrations
        userModel.tag = tagEntity

        return userModel
    }

    /**
     * Used to find multiple users with one tag
     * @return Set<UserModel> - users with tag name like input
     *
     * @author Danil Kiselev
     * */
    fun findByTag(tagId: UUID): Set<UserModel> {

        val ents = db.users.filter { Users.tagId eq tagId }.toSet()
        var models: MutableSet<UserModel> = mutableSetOf<UserModel>()
        ents.forEach {
            val user = converter.entityToModel(it, null)
            user.integrations = findSetOfIntegrationsByUser(user.id!!)
            models.add(user)

        }
        return models
    }

    /**
     * Searching user with input integration value.
     * @return UserModel - user with integration value eq input line
     * @throws InstanceNotFoundException if there are no users with such integration value
     *
     * @author Danil Kiselev
     * */
    fun findByEmail(email: String): UserModel {
        val integrationUserEntity: UserIntegrationEntity =
            db.usersinegrations.find { it.valueStr eq email } ?: throw InstanceNotFoundException(
                UserIntegrationEntity::class, "Integration with value ${email} not found"
            );
        return findById(integrationUserEntity.userId.id)
    }

    /**
     * Returns Integration entity by id
     * @return IntegrationEntity
     * @throws InstanceNotFoundException(IntegrationEntity, "Integration with id ${id} not found")
     *
     * @author Danil Kiselev
     * */
    private fun findIntegrationById(id: UUID): IntegrationEntity {
        return db.integrations.find { it.id eq id } ?: throw InstanceNotFoundException(
            IntegrationEntity::class, "Integration with id ${id} not found"
        )
    }

    /**
     * Returns all integrations of user (by user id)
     * @return MutableSet<IntegrationModel>
     * @throws InstanceNotFoundException(UserEntity::class, "User $userId")
     *
     * @author Danil Kiselev
     * */
    fun findSetOfIntegrationsByUser(userId: UUID): MutableSet<IntegrationModel> {
        if (!existsById(userId)) {
            throw InstanceNotFoundException(UserEntity::class, "User $userId")
        }
        val usersIntegrationsSet: Set<UserIntegrationEntity> =
            db.usersinegrations.filter { it.userId eq userId }.toSet()
        var modelsSet: MutableSet<IntegrationModel> = mutableSetOf()

        usersIntegrationsSet.forEach {
            val integrationEntity = findIntegrationById(it.integrationId.id!!)
            modelsSet.add(
                IntegrationModel(
                    id = integrationEntity.id!!,
                    name = integrationEntity.name,
                    valueStr = it.valueStr,
                    iconUrl = integrationEntity.iconUrl
                )
            )
        }
        return modelsSet;
    }

    /**
     * Returns TagModel by value
     * @return UserTagModel
     * @throws InstanceNotFoundException(UsersTagEntity::class, "Tag with name ${tagName} not found")
     *
     * @author Danil Kiselev
     * */
    fun findTagByName(tagName: String): UserTagModel {
        val tag = db.users_tags.find { it.name eq tagName } ?: throw InstanceNotFoundException(
            UsersTagEntity::class, "Tag with name ${tagName} not found"
        )
        return UserTagModel(tag.id, tag.name)
    }

    fun updateUser(model: UserModel): UserModel {
        val userid: UUID = model.id ?: throw UserNotFoundException("No id in the model")
        if (!existsById(userid)) {
            throw UserNotFoundException("User ${model.fullName} with id:${userid} does not exists")
        }
        var ent = db.users.find { it.id eq userid }
        ent?.tag = model.tag
        ent?.fullName = model.fullName
        ent?.active = model.active
        ent?.avatarURL = model.avatarURL
        ent?.role = model.role
        ent?.flushChanges()

        val integrations: Set<IntegrationModel>? = model.integrations
        if (!integrations.isNullOrEmpty()) {
            val converter: IntegrationModelEntityConverter = GlobalContext.get().get()
            db.usersinegrations.filter { it.userId eq userid }.forEach { it.delete() }
            for (i in integrations) {
                db.insert(UsersIntegrations) {
                    set(it.userId, userid)
                    set(it.integrationId, i.id)
                    set(it.valueStr, i.valueStr)
                }
            }
        }
        return findById(userid)
    }

    /**
     * Returns Tag entity or null, if user with such id doesn't exist
     * @return UsersTagEntity?
     *
     * @author Danil Kiselev
     * */
    fun findTagByUserOrNull(userId: UUID): UsersTagEntity? {
        if (!existsById(userId)) return null
        val ent = findById(userId)
        return ent.tag
    }
}
