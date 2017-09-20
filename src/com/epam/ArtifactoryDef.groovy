#!/usr/bin/groovy
package com.epam


//@Grapes(
//        @Grab('org.jenkins-ci.plugins:pipeline-utility-steps:1.4.1')
)
//
@Grapes(
        @Grab('org.jfrog.artifactory.client:artifactory-java-client-api:2.5.2')
)

import org.jfrog.artifactory.client.Artifactory


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

    def ArtifactoryServer = Artifactory.newServer("${ArtifactoryAddress}", "${env.ARTIFACTORY_USER}", "${env.ARTIFACTORY_PWD}")
    def buildInfo = Artifactory.newBuildInfo()
    buildInfo.env.capture = true
    def done = ArtifactoryServer.upload("${env.UPLOAD_SPEC}")

    env.setProperty("${"TIMESTAMP"}", TIMESTAMP)
    env.setProperty("${"PROJECT_NAME"}", projectName)
    env.setProperty("${"ARTIFACTORY_ADDRESS"}", ArtifactoryAddress)
    env.setProperty("${"UPLOAD_SPEC"}", uploadSpec)
    return done
}

//def ArtifactoryServer = Artifactory.newServer("${env.ARTIFACTORY_ADDRESS}", "${env.ARTIFACTORY_USER}", "${env.ARTIFACTORY_PWD}")
//def buildInfo = Artifactory.newBuildInfo()
//buildInfo.env.capture = true
//ArtifactoryServer.upload("${env.UPLOAD_SPEC}")