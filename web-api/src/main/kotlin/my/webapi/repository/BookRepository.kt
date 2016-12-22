package my.webapi.repository

import my.common.db.BookTable
import my.common.domain.Book
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
open class BookRepository {
    open fun findAll(): Iterable<Book> = BookTable.selectAll().map { it.fromRow() }

    open fun findById(id: Long): Book? = BookTable.select { BookTable.id eq id }.singleOrNull()?.fromRow()

    open fun create(source: Book): Book = source.apply {
        id = BookTable.insert(source.toRow())[BookTable.id]
    }

    open fun update(source: Book): Int = BookTable.update({ BookTable.id eq source.id!! }) {
        it[name] = source.name
        it[amount] = source.amount
    }

    open fun deleteById(id: Long): Int = BookTable.deleteWhere { BookTable.id eq id }

    private fun Book.toRow(): BookTable.(UpdateBuilder<*>) -> Unit = {
        it[name] = this@toRow.name
        it[amount] = this@toRow.amount
    }

    private fun ResultRow.fromRow(): Book = Book(this[BookTable.id], this[BookTable.name], this[BookTable.amount])
}

/*
class BookEntity(id: EntityID<Long>) : Entity<Long>(id) {
    companion object: EntityClass<Long, BookEntity>(BookTable)

    var name by BookTable.name
    var amount by BookTable.amount
}
*/
