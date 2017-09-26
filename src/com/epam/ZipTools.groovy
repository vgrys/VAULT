//package ctc.ad.corp.cicd
package com.epam

import java.text.SimpleDateFormat

static def bundle(env, incl, excl) {
    def filesToZip = ["${incl}"]
    def TIMESTAMP = new SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
    def jobBaseName = "${env.JOB_NAME}".split('/')
    def projectName = "${jobBaseName[0]}"
    String zipFilePath = "${env.WORKSPACE}/${projectName}_${TIMESTAMP}.zip"
    String sourceFolder = "${env.WORKSPACE}"

//    (new AntBuilder()).zip(destfile: zipFilePath, basedir: sourceFolder)
    new AntBuilder().zip(destfile: zipFilePath,
            basedir: sourceFolder,
            includes: filesToZip.join(' '),
            excludes: "${excl}")

    return zipFilePath
}


//def HOME = 'home'
//def deploymentFiles = [ 'Songs/A/a1.tsv', 'Songs/B/b1.tsv' ]
//def zipFile = new File("deployment_zipFile.zip")
//new AntBuilder().zip( basedir: HOME,
//        destFile: zipFile.absolutePath,
//        includes: deploymentFiles.join( ' ' ) )