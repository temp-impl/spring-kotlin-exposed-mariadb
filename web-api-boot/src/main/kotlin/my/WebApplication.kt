package my

import my.webapi.repository.DbSetup
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Suppress("unused")
@SpringBootApplication
@EnableTransactionManagement
open class WebApplication {
    @Bean
    open fun transactionManager(dataSource: DataSource) = SpringTransactionManager(dataSource)

    @Bean
    open fun init(setup: DbSetup) = CommandLineRunner {
        setup.createTables()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(WebApplication::class.java, *args)
}
