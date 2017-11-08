#!/usr/bin/groovy

def static artifactoryConfig(env, repository, String archive, name, version) {

    env.uploadSpec = """{"files": [{
                        "pattern": "${archive}",
                        "target": "artifactory/${repository}/${name}/${version}/"
                    }]}"""
}

def static artifactoryATFConfig(env, repository, String archive, String name) {
    def branchDirs = [
            'develop': 'develop',
            'master' : 'stable',
            'release': 'release'
    ]
    def dirName = branchDirs.get(env.GIT_BRANCH_TYPE, '')
    if (dirName != '') {
        artifactoryConfig(env, repository, archive, name, dirName)
    }
}

def static artifactoryProjectConfig(env, repository, String archive, name) {
    artifactoryConfig(env, repository, archive, "${env.GIT_REPO}", '')
}

def ATFUpload (artifactoryUrl, artifactoryRepo) {
    GString archive = "${env.WORKSPACE}/dist/*.tar.gz"
    def server = Artifactory.newServer url: "${artifactoryUrl}", credentialsId: 'arifactoryID'
    artifactoryATFConfig(env, artifactoryRepo, archive, "atf")
    server.upload(uploadSpec)
}

def projectUpload (artifactoryUrl, artifactoryRepo, name) {
    GString archive = "${env.WORKSPACE}/*.tgz"
    def server = Artifactory.newServer url: "${artifactoryUrl}", credentialsId: 'arifactoryID'
    artifactoryProjectConfig(env, artifactoryRepo, archive, name)
    server.upload(uploadSpec)
}

def ansibleUpload (artifactoryUrl, artifactoryRepo, name) {
    GString archive = "${env.WORKSPACE}/*.tgz"
    def server = Artifactory.newServer url: "${artifactoryUrl}", credentialsId: 'arifactoryID'
    artifactoryATFConfig(env, artifactoryRepo, archive, name)
    server.upload(uploadSpec)
    return (uploadSpec)
}

def ansibleDownload (artifactoryUrl, artifactoryRepo, name, version) {
    GString frameworkArtifactoryPath = "${artifactoryRepo}/${name}/${version}/*.tgz"
    GString downloadSpec = """{"files": [{"pattern": "${frameworkArtifactoryPath}", "target": "${env.WORKSPACE}/ansible/"}]}"""
    def server = Artifactory.newServer url: "${artifactoryUrl}/artifactory/", credentialsId: 'arifactoryID'
    server.download(downloadSpec)
}