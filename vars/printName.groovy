#!/usr/bin/groovy

def call(body) {

    def config = [:]
    body()
    println("\n$USER")
}
