#!/usr/bin/groovy

def call(body) {

    def config = [:]
    body()
    println("\nHello World")
    println("\nTesting Library")
}
