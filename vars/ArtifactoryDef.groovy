#!/usr/bin/groovy
package com.epam


def call(String name = 'human') {
    // Any valid steps can be called from this code, just like in other
    // Scripted Pipeline
    echo "Hello, ${name}."
}

//def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
//
//
//def jobBaseName = "${env.JOB_NAME}".split('/')


