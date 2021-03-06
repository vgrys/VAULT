#!/usr/bin/groovy

def static artifactoryConfig(env, repository, String archive, name, version) {

    env.uploadSpec = """{"files": [{
                        "pattern": "${archive}",
                        "target": "artifactory/${repository}/${name}/${version}/"
                    }]}"""
}

def artifactoryATFConfig(env, repository, String archive, String name) {
    def branchDirs = [
            develop: 'develop',
            master : 'stable',
            release: 'release',
            feature: 'feature'
    ]
    def dirName = branchDirs.get(env.GIT_BRANCH_TYPE)
    if (dirName) {
        artifactoryConfig(env, repository, archive, name, dirName)
    }
}

def static artifactoryProjectConfig(env, repository, String archive, name) {
    artifactoryConfig(env, repository, archive, name, '')
}


def ATFUpload (artifactoryUrl, artifactoryRepo, artifactoryId) {
    GString archive = "${env.WORKSPACE}/dist/*.tar.gz"
    def server = Artifactory.newServer url: "${artifactoryUrl}", credentialsId: "${artifactoryId}"
    artifactoryATFConfig(env, artifactoryRepo, archive, "atf")
    server.upload(uploadSpec)
}

def projectUpload (artifactoryUrl, artifactoryRepo, name, artifactoryId) {
    GString archive = "${env.WORKSPACE}/*.tgz"
    def server = Artifactory.newServer url: "${artifactoryUrl}", credentialsId: "${artifactoryId}"
    artifactoryProjectConfig(env, artifactoryRepo, archive, name)
    server.upload(uploadSpec)
}

def uploadAnsible (artifactoryUrl, artifactoryRepo, name, artifactoryId) {
    GString archive = "${env.WORKSPACE}/*.tgz"
    def server = Artifactory.newServer url: "${artifactoryUrl}", credentialsId: "${artifactoryId}"
    artifactoryATFConfig(env, artifactoryRepo, archive, name)
    server.upload(uploadSpec)
    return (uploadSpec)
}

def ansibleDownload (artifactoryUrl, artifactoryRepo, name, release, version) {
    GString frameworkArtifactoryPath = "${artifactoryRepo}/${name}/${release}/${name}-${version}.tgz"
    GString downloadSpec = """{"files": [{"pattern": "${frameworkArtifactoryPath}", "target": "${env.WORKSPACE}/ansible/"}]}"""
    def server = Artifactory.newServer url: "${artifactoryUrl}/artifactory/", credentialsId: 'arifactoryID'
    server.download(downloadSpec)
}