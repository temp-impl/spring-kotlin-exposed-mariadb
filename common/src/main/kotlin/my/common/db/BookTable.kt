package my.common.db

import org.jetbrains.exposed.sql.Table

object BookTable : Table("book") {
    val id = long("id").autoIncrement().primaryKey()
    val name = varchar("name", 100)
    val amount = integer("amount")
}
