#!/usr/bin/groovy
package com.epam.ArtifactoryToolsPlugin


def static artifactoryConfig(env, repository, String atfArchivePath, String projectArchivePath, atf_version, project_name, project_version) {

    def branchName = "${env.BRANCH_NAME}".split('/')
    def releaseBranchName = "${branchName[0]}"

    if (${env.BRANCH_NAME} == 'develop') {
        directory = develop
    } else if (${env.BRANCH_NAME} == 'master') {
        directory = stable
    } else if (releaseBranchName == 'release') {
        directory = release
    }

    def artifactoryATFPath = "artifactory/${repository}/atf/${directory}/"
    def artifactoryProjectPath = "artifactory/${repository}/${project_name}/${project_version}/"


    env.uploadSpec = """{
                    "files": [{
                        "pattern": "${atfArchivePath}",
                        "target": "${artifactoryATFPath}/"
                    },
                    {
                        "pattern": "${projectArchivePath}",
                        "target": "${artifactoryProjectPath}/${project_name}-${project_version}.tgz"
                    }]
                 }"""
}
//when built from master - deploy to atf/stable/atf-*tar.gz
//when built from develop - deploy to atf/develop/atf-*tar.gz
//when built from release - deploy to atf/release/atf-*tar.gz (each release will upgrade version so files will stay unique)
