#!/usr/bin/env groovy

static def call(String buildStatus = 'TIMESTAMP') {
    def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
}