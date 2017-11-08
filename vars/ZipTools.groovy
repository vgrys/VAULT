#!/usr/bin/groovy

def extractAnsible(frameworkName, frameworkVersion) {
    sh "tar -xzf ${env.WORKSPACE}/ansible//${frameworkName}/${frameworkVersion}/${frameworkName}-${frameworkVersion}.tgz -C ${env.WORKSPACE}/ansible/"
}

def bundle(sourceFolder, excludes, bundleName = '') {

    String excludeParameters = ''
    for (exclude in excludes) {
        excludeParameters += "--exclude='${exclude}' "
    }

    if (bundleName == '') {
        def now = new Date()
        String timestamp = now.format('yyyyMMddHHmmss')
        def jobBaseName = "${env.JOB_NAME}".split('/')
        GString projectName = "${jobBaseName[0]}"
        bundleName = "${projectName}-${timestamp}.tgz"
    }
    GString archhiveFilePath = "${env.WORKSPACE}/${bundleName}"

    sh "cd ${sourceFolder} && tar -zcf ${archhiveFilePath} ${excludeParameters} * "

    return bundleName
}
