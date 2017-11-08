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

def extractAnsible(name, release, version) {
    sh "tar -xzf ${env.WORKSPACE}/ansible/${name}/${release}/${name}-${version}.tgz -C ${env.WORKSPACE}/ansible/"
}