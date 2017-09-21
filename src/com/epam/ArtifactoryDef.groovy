#!/usr/bin/groovy
package com.epam


@Grapes([
        @Grab(group='commons-io', module='commons-io', version='2.5'),
        @Grab(group='org.codehaus.groovy', module='groovy-xml', version='2.3.2'),
        @Grab(group='org.jfrog.artifactory.client', module='artifactory-java-client-services', version='2.5.2'),
        @Grab(group='org.jfrog.artifactory.client', module='artifactory-java-client-api', version='2.5.2'),
])

import org.jfrog.artifactory.client.Artifactory
import org.jfrog.artifactory.client.ArtifactoryClient
import org.jfrog.artifactory.client.model.File

static def upload_atrifact(env, atifactory_ip, repository, artifact) {

    java.io.File bundle = new java.io.File(artifact)
    def ArtifactoryUploadPath = "${env.JOB_NAME}/${env.BUILD_NUMBER}/${bundle.getName()}"


    Artifactory artifactory = ArtifactoryClient.create("${atifactory_ip}/artifactory/", "${env.ARTIFACTORY_USER}", "${env.ARTIFACTORY_PWD}")
    File result = artifactory.repository("${repository}").upload("${ArtifactoryUploadPath}", bundle).doUpload()

    return result.getSize() //.getDownloadUri()
}



//import    step.setArchive(true)
