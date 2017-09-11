#!/usr/bin/groovy

def call(body) {
    body()
    println("\n$USER")
}
