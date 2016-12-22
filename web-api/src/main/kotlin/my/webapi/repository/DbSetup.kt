package my.webapi.repository

import my.common.db.BookTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
open class DbSetup {
    open fun createTables() = SchemaUtils.create(BookTable)
}
