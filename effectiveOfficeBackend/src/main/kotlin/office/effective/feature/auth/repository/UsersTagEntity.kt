package office.effective.feature.auth.repository

import office.effective.feature.auth.repository.UsersTags.primaryKey
import org.ktorm.database.Database
import org.ktorm.dsl.isNotNull
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar
import java.util.UUID

interface UsersTagEntity : Entity<UsersTagEntity> {
    companion object : Entity.Factory<UsersTagEntity>()

    val id: UUID
    val name: String
}

object UsersTags : Table<UsersTagEntity>("users_tags") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }.isNotNull()
}

val Database.users_tags get() = this.sequenceOf(UsersTags)
