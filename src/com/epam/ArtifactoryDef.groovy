#!/usr/bin/groovy
package com.epam


@Grapes([
        @Grab(group='org.jfrog.artifactory.client', module='artifactory-java-client-services', version='2.5.2'),
        @Grab(group='org.jfrog.artifactory.client', module='artifactory-java-client-api', version='2.5.2'),
        @Grab(group='org.codehaus.groovy', module='groovy-xml', version='2.3.2'),
        @Grab(group='org.jenkins-ci.plugins', module='pipeline-utility-steps', version='1.4.1'),
        @Grab(group='org.codehaus.plexus', module='plexus-utils', version='3.1.0'),
        @Grab(group='commons-io', module='commons-io', version='2.5')
])

import org.jenkinsci.plugins.pipeline.utility.steps.zip.ZipStep
import org.jfrog.artifactory.client.Artifactory
import org.jfrog.artifactory.client.ArtifactoryClient
import org.jfrog.artifactory.client.model.File

static def upload_atrifact(env, atifactory_ip, repository, java.io.File artifact) {
//    def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
//    def jobBaseName = "${env.JOB_NAME}".split('/')
//    def projectName = "${jobBaseName[0]}"

    def ArtifactoryUploadPath = "${env.JOB_NAME}/${env.BUILD_NUMBER}/${artifact.getName()}"
//    def ArtifactoryAddress = "${atifactory_ip}/artifactory/"


//    ZipStep step = new ZipStep("TEMP123.zip")
//            step.setDir('/home/vagrant/TEST/')
//            step.setArchive(true)

    Artifactory artifactory = ArtifactoryClient.create("${atifactory_ip}/artifactory/", "${env.ARTIFACTORY_USER}", "${env.ARTIFACTORY_PWD}")
    File result = artifactory.repository("${repository}").upload("${ArtifactoryUploadPath}", artifact).doUpload()

//    env.setProperty("${"TIMESTAMP"}", TIMESTAMP)
//    env.setProperty("${"PROJECT_NAME"}", projectName)
//    env.setProperty("${"ARTIFACTORY_ADDRESS"}", ArtifactoryAddress)
    return result.getSize() //.getDownloadUri()
}