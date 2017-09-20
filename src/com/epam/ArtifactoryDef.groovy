#!/usr/bin/groovy
package com.epam

//@Grapes(
//        @Grab('org.jenkins-ci.plugins:pipeline-utility-steps:1.4.1')
//)

@Grapes([
        @Grab(group='org.jfrog.artifactory.client', module='artifactory-java-client-services', version='2.3.5'),
        @Grab(group='org.jenkins-ci.plugins', module='pipeline-utility-steps', version='1.4.1'),
        @Grab(group='org.codehaus.plexus', module='plexus-utils', version='3.1.0'),
        @Grab(group='commons-io', module='commons-io', version='2.5')
])

import org.jenkinsci.plugins.pipeline.utility.steps.zip.ZipStep

import org.jfrog.artifactory.client.ArtifactoryClient
import org.jfrog.artifactory.client.impl.ArtifactoryImpl
import org.jfrog.artifactory.client.impl.UploadableArtifactImpl

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


    ZipStep step = new ZipStep("${projectName}-${TIMESTAMP}.zip");
    step.setDir("");
//        step.setGlob("**/*.zip");
    step.setArchive(true);

    env.setProperty("${"TIMESTAMP"}", TIMESTAMP)
    env.setProperty("${"PROJECT_NAME"}", projectName)
    env.setProperty("${"ARTIFACTORY_ADDRESS"}", ArtifactoryAddress)
    env.setProperty("${"UPLOAD_SPEC"}", uploadSpec)

}

//    ArtifactoryImpl artifactory = ArtifactoryClient.create("${ArtifactoryAddress}", "${env.ARTIFACTORY_USER}", "${env.ARTIFACTORY_PWD}")
//    java.io.File file = new java.io.File("*.zip")
//    File result = artifactory.repository("${repository}").upload("${ArtifactoryUploadPath}", file).doUpload()

//    def buildInfo = Artifactory.newBuildInfo()
//    buildInfo.env.capture = true
//    def done = artifactory.upload("${env.UPLOAD_SPEC}")


//def ArtifactoryServer = Artifactory.newServer("${env.ARTIFACTORY_ADDRESS}", "${env.ARTIFACTORY_USER}", "${env.ARTIFACTORY_PWD}")
//def buildInfo = Artifactory.newBuildInfo()
//buildInfo.env.capture = true
//ArtifactoryServer.upload("${env.UPLOAD_SPEC}")