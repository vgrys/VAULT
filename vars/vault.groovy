#!/usr/bin/groovy
// @Grapes(
//     @Grab(group='com.bettercloud', module='vault-java-driver', version='3.0.0')
// )


@Grab(group='org.springframework', module='spring-orm', version='3.2.5.RELEASE')
import org.springframework.jdbc.core.JdbcTemplate

def call(body) {

    def config = [:]
    body()
    println("\nHello World")
    println("\nTesting Library")
}
