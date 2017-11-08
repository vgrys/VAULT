#!/usr/bin/groovy

def bundle(sourceFolder, excludes, bundleName = '') {

    String excludeParameters = ''
    for (exclude in excludes) {
        excludeParameters += "--exclude='${exclude}' "
    }

    if (bundleName == '') {
        def now = new Date()
        String timestamp = now.format('yyyyMMddHHmmss')
        bundleName = "${env.GIT_REPO}-${timestamp}.tgz"
    }
    GString archhiveFilePath = "${env.WORKSPACE}/${bundleName}"

    sh "cd ${sourceFolder} && tar -zcf ${archhiveFilePath} ${excludeParameters} * "

    return bundleName
}

def extractAnsible(frameworkName, playbooksRelease, frameworkVersion) {
    sh "tar -xzf ${env.WORKSPACE}/ansible/${frameworkName}/${playbooksRelease}/${frameworkName}-${frameworkVersion}.tgz -C ${env.WORKSPACE}/ansible/"
}