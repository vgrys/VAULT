#!/usr/bin/groovy
package com.epam


def call(body) {
    body()
    println("\n$USER")
}


//def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
//
//
//def jobBaseName = "${env.JOB_NAME}".split('/')


