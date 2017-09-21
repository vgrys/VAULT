package com.epam

//@Grapes([
//        @Grab(group='xerces', module='xerces', version='2.4.0'),
//
//])

static def bundle(env) {
    def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
    def jobBaseName = "${env.JOB_NAME}".split('/')
    def projectName = "${jobBaseName[0]}"
    String zipFilePath = "${env.WORKSPACE}/${projectName}_${TIMESTAMP}.zip"
    String sourceFolder = "${env.WORKSPACE}"

    (new AntBuilder()).zip(destfile: zipFilePath, basedir: sourceFolder)

    return zipFilePath
}