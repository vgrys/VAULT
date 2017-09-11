#!/usr/bin/groovy
// @Grapes(
//     @Grab(group='com.bettercloud', module='vault-java-driver', version='3.0.0')
// )


@Grab('org.apache.httpcomponents:httpclient:4.2.1')

def call(body) {

    def config = [:]
    body()
    println("\nHello World")
    println("\nTesting Library")
}
