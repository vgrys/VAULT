
def bundle(env, sourceFolder, excludes, bundleName = '') {

    String excludeParameters = ''
    for (exclude in excludes) {
        excludeParameters += "--exclude='${exclude}' "
    }

    if (bundleName == '') {
        def now = new Date()
        String timestamp = now.format('yyyyMMddHHmmss')
        def jobBaseName = "${env.JOB_NAME}".split('/')
        GString projectName = "${jobBaseName[0]}"
        bundleName = "${projectName}_${timestamp}"
    }
    GString archhiveFilePath = "${env.WORKSPACE}/${bundleName}.tgz"

    sh "cd ${sourceFolder} && tar -zcf ${archhiveFilePath} ${excludeParameters} * "

    return archhiveFilePath
}