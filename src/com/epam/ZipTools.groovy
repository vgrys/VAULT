//package ctc.ad.corp.cicd
package com.epam

import java.text.SimpleDateFormat

static def bundle(env) {
    def TIMESTAMP = new SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
    def jobBaseName = "${env.JOB_NAME}".split('/')
    def projectName = "${jobBaseName[0]}"
    String zipFilePath = "${env.WORKSPACE}/${projectName}_${TIMESTAMP}.zip"
    String sourceFolder = "${env.WORKSPACE}"

//    (new AntBuilder()).zip(destfile: zipFilePath, basedir: sourceFolder)
    new AntBuilder().zip(destfile: zipFilePath) {
        fileset (dir: sourceFolder)
            include(name:zipFilePath.name)
            exclude(name:"**/*.py")
}


    return zipFilePath
}

//new AntBuilder().zip( destFile: "${file}.zip" ) {
//    fileset( dir: './Testing' ) {
//        include( name:file.name )
//    }
//}