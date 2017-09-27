#!/usr/bin/groovy
package com.epam

@Grapes([
        @Grab(group='commons-io', module='commons-io', version='2.5'),
        @Grab(group='org.jfrog.artifactory.client', module='artifactory-java-client-services', version='2.5.2'),
        @Grab(group='org.jfrog.artifactory.client', module='artifactory-java-client-api', version='2.5.2'),
        @GrabExclude(group='org.codehaus.groovy', module='groovy-xml'),
        @GrabExclude(group='xerces', module='xercesImpl')
])

import org.jfrog.artifactory.client.Artifactory
import org.jfrog.artifactory.client.ArtifactoryClientBuilder
import org.jfrog.artifactory.client.model.File

static def upload(env, atifactory_ip, repository, String artifactPath, ARTIFACTORY_USER, ARTIFACTORY_PWD) {
//    def atfArtifact = "${env.WORKSPACE}/dist/*.tar.gz"

    def atfArtifact = new FileNameFinder().getFileNames("${env.WORKSPACE}", '**/*.tar.gz')
//    assert new java.io.File("${env.WORKSPACE}", 'README.txt').absolutePath in txtFiles

    return atfArtifact

    java.io.File projectartifact = new java.io.File(artifactPath)
    java.io.File atfartifact = new java.io.File(atfArtifact)
    def ArtifactoryUploadPath = "${env.JOB_NAME}/${env.BUILD_NUMBER}/${projectartifact.getName()}"

    Artifactory artifactory = ArtifactoryClientBuilder.create()
        .setUrl("${atifactory_ip}/artifactory/")
        .setUsername("${ARTIFACTORY_USER}")
        .setPassword("${ARTIFACTORY_PWD}")
        .build()

File result = artifactory.repository("${repository}").upload("${ArtifactoryUploadPath}", projectartifact).doUpload()

return result.getDownloadUri()
}