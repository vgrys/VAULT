package com.epam


// vars/acme.groovy
def setName(value) {
    name = value
}
def getName() {
    name
}
def caution(message) {
    echo "Hello, ${name}! CAUTION: ${message}"
}

//def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
//
//
//def jobBaseName = "${env.JOB_NAME}".split('/')


