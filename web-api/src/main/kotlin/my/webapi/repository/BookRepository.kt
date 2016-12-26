package my.webapi.repository

import my.common.db.BookTable
import my.common.domain.Book
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
open class BookRepository {
    /**
     * SELECT * FROM BookTable
     */
    open fun findAll(): Iterable<Book> = BookTable.selectAll().map { it.fromRow() }

    /**
     * SELECT * FROM BookTable WHERE id = :id
     */
    open fun findById(id: Long): Book? = BookTable.select { BookTable.id eq id }.singleOrNull()?.fromRow()

    /**
     * INSERT INTO BookTable VALUES(:source)
     */
    open fun create(source: Book): Book = source.apply {
        id = BookTable.insert(source.toRow())[BookTable.id]
    }

    /**
     * UPDATE BookTable
     * SET name = :source.name, amount = :source.amount
     * WHERE id = :source.id
     */
    open fun update(source: Book): Int = BookTable.update({ BookTable.id eq source.id!! }) {
        it[name] = source.name
        it[amount] = source.amount
    }

    /**
     * UPDATE BookTable
     * SET amount = amount + :value
     * WHERE id = :id
     */
    open fun addAmount(id: Long, value: Int): Int = BookTable.update({ BookTable.id eq id }) {
        with(SqlExpressionBuilder) {
            it.update(BookTable.amount, BookTable.amount + value)
        }
    }

    /**
     * DELETE FROM BookTable WHERE id = :id
     */
    open fun deleteById(id: Long): Int = BookTable.deleteWhere { BookTable.id eq id }

    /**
     * Raw query sample
     */
    open fun version(): String {
        val conn = TransactionManager.current().connection
        val statement = conn.createStatement()
        statement.execute("SELECT VERSION();")
        return statement.resultSet.let { it.next(); it.getString(1) }
    }

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
