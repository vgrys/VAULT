#!/usr/bin/groovy
package com.epam


@Grapes(
        @Grab('org.jenkins-ci.plugins:pipeline-utility-steps:1.4.1')
)
import org.jenkinsci.plugins.pipeline.utility.steps.*

static def configure_artifactory(env, atifactory_ip, repository) {
    def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
    def jobBaseName = "${env.JOB_NAME}".split('/')
    def projectName = "${jobBaseName[0]}"
    def ArtifactoryUploadPath = "${env.JOB_NAME}/${env.BUILD_NUMBER}/"
    def ArtifactoryAddress = "${atifactory_ip}/artifactory/${repository}"

    def uploadSpec = """{
                            "files": [{
                                "pattern": "*.zip",
                                "target": "${ArtifactoryUploadPath}"
                             }]
                        }"""

    zip archive: true, zipFile: "${projectName}-${TIMESTAMP}.zip", dir: ''

    env.setProperty("${"TIMESTAMP"}", TIMESTAMP)
    env.setProperty("${"PROJECT_NAME"}", projectName)
    env.setProperty("${"ARTIFACTORY_ADDRESS"}", ArtifactoryAddress)
    env.setProperty("${"UPLOAD_SPEC"}", uploadSpec)
}