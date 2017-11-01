//package ctc.ad.corp.cicd
package com.epam

static def bundle(env, sourceFolder, excludes, sh) {
//    def filesToInclude = [incl]
//    def filesToExclude = ['**/*.groovy', '**/tests/*', '**/*__init__*']


    def now = new Date()
    String timestamp = now.format('yyyyMMddHHmmss')
    def jobBaseName = "${env.JOB_NAME}".split('/')
    GString projectName = "${jobBaseName[0]}"
    GString archhiveFilePath = "${env.WORKSPACE}/${projectName}_${timestamp}.tgz"
//
//    new AntBuilder().zip(destfile: zipFilePath, basedir: sourceFolder, excludes: excludes)

    sh "tar --exclude=${excludes},  -zcvf ${archhiveFilePath} -C ${sourceFolder} *"

    return archhiveFilePath
}

