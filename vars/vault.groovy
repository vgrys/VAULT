#!/usr/bin/groovy

def call(body) {

    def config = [:]
    body()
    println("Hello World")
}
