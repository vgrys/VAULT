#!/usr/bin/groovy

def static artifactoryConfig(env, repository, String archive, name, version) {

    env.uploadSpec = """{
                    "files": [{
                        "pattern": "${archive}",
                        "target": "artifactory/${repository}/${name}/${version}/"
                    }]
                    }"""
}

def static artifactoryATFConfig(env, repository, String archive) {
    def branchDirs = [
            'develop': 'develop',
            'master' : 'stable',
            'release': 'release'
    ]
    def dirName = branchDirs.get(env.GIT_BRANCH_TYPE, '')
    if (dirName != '') {
        artifactoryConfig(repository, archive, "atf", dirName)
    }
}

def static artifactoryProjectConfig(env, repository, String archive, name, version) {
    artifactoryConfig(env, repository, archive, name, version)
}

def ATFUpload (artifactoryUrl, artifactoryRepo) {
    GString atfArchivePath = "${env.WORKSPACE}/dist/*.tar.gz"
    def server = Artifactory.newServer url: "${artifactoryUrl}", credentialsId: 'arifactoryID'
    def artifactory = new artifactoryTools()
    artifactory.artifactoryATFConfig(env, artifactoryRepo, atfArchivePath)
    server.upload(uploadSpec)
}

def projectUpload (artifactoryUrl, artifactoryRepo, projectName, projectVersion) {
    GString projectArchivePath = "${env.WORKSPACE}/*.tgz"
    def server = Artifactory.newServer url: "${artifactoryUrl}", credentialsId: 'arifactoryID'
    def artifactory = new artifactoryTools()
    artifactory.artifactoryProjectConfig(env, artifactoryRepo, projectArchivePath, projectName, projectVersion)
    server.upload(uploadSpec)
}