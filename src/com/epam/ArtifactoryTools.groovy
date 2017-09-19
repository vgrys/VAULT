package com.epam


def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())

//def TIMESTAMP () {
//    new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
//}

def jobBaseName = "${env.JOB_NAME}".split('/')



