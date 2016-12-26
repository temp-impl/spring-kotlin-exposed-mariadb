package my.webapi.controllers

import my.webapi.repository.BookRepository
import my.common.domain.Book
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Suppress("unused")
@RestController
@RequestMapping("/book")
open class BookController(
        val repository: BookRepository
) {
/*
    @Autowired
    lateinit private var bookRep: BookRepository
*/
    @GetMapping
    open fun get(): Iterable<Book> = repository.findAll()

    @GetMapping("/{id:\\d+}")
    open fun get(@PathVariable id: Long): Book? = repository.findById(id)

    @GetMapping("/{id:\\d+}/amount")
    open fun addAmount(@PathVariable id: Long, @RequestParam(required = false) value: Int?): Int = repository.addAmount(id, value ?: 1)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    open fun post(@RequestBody book: Book): Book = repository.create(book)

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    open fun put(@RequestBody book: Book){ repository.update(book) }

    @DeleteMapping("/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    open fun delete(@PathVariable id: Long){ repository.deleteById(id) }

    @GetMapping("/version")
    open fun version(): String = repository.version()
}
