#!/usr/bin/groovy
package com.epam.ArtifactoryToolsPlugin

//def static artifactoryConfig(env, repository, String atfArchivePath, String projectArchivePath, atf_version, project_name, project_version) {
//
//    def branchDirs = [
//        'develop': 'develop',
//        'master': 'stable',
//        'release': 'release'
//    ]
//
//    def dirName = branchDirs.get(env.GIT_BRANCH_TYPE, '')
//    if (dirName != '') {
//        GString artifactoryATFPath = "artifactory/${repository}/atf/${dirName}/"
//        GString artifactoryProjectPath = "artifactory/${repository}/${project_name}/${project_version}/"
//
//
//        env.uploadSpec = """{
//                    "files": [{
//                        "pattern": "${atfArchivePath}",
//                        "target": "${artifactoryATFPath}/"
//                    },
//                    {
//                        "pattern": "${projectArchivePath}",
//                        "target": "${artifactoryProjectPath}/${project_name}-${project_version}.tgz"
//                    }]
//                 }"""
//    }
//}


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
        artifactoryConfig(env, repository, archive, "atf", branchDirs)
    }
}

def static artifactoryProjectConfig(env, repository, String archive, name, version) {
    artifactoryConfig(env, repository, archive, name, version)
}