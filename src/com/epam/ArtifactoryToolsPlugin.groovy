#!/usr/bin/groovy
package com.epam.ArtifactoryToolsPlugin


static def artifactoryConfig(env, repository, String atfArchivePath, String projectArchivePath, atf_version, project_name, project_version) {

//    def jobBaseName = "${env.JOB_NAME}".split('/')
//    def projectName = "${jobBaseName[0]}"
    def artifactoryATFPath = "artifactory/${repository}/atf/${atf_version}/"
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
