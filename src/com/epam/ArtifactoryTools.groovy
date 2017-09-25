#!/usr/bin/groovy
package com.epam

@Grapes([
        @Grab(group='commons-io', module='commons-io', version='2.5'),
        @Grab(group='org.jfrog.artifactory.client', module='artifactory-java-client-services', version='2.5.2'),
        @Grab(group='org.jfrog.artifactory.client', module='artifactory-java-client-api', version='2.5.2'),
//        @Grab(group='com.fasterxml.jackson.core', module='jackson-core', version='2.4.6'),
//        @GrabExclude(group='com.fasterxml.jackson.core', module='jackson-core', version='2.9.1'),
        @GrabExclude(group='org.codehaus.groovy', module='groovy-xml'),
        @GrabExclude(group='xerces', module='xercesImpl')
])

import org.jfrog.artifactory.client.Artifactory
import org.jfrog.artifactory.client.ArtifactoryClientBuilder
import org.jfrog.artifactory.client.model.File
import com.fasterxml.jackson.databind.ObjectMapper

static def upload(env, atifactory_ip, repository, String artifactPath, ARTIFACTORY_USER, ARTIFACTORY_PWD) {

    def a = []
//    def b = new ArtifactoryTools()
//    b.class.getClassLoader().getURLs().each { url ->
//        a.add "- ${url.toString()}"
//    }
//    return a

    ObjectMapper.class.declaredMethods.findAll{ !it.synthetic }.each {
        a.add("$it.name $it.parameters.name")
    }

    return a
//
//    java.io.File artifact = new java.io.File(artifactPath)
//    def ArtifactoryUploadPath = "${env.JOB_NAME}/${env.BUILD_NUMBER}/${artifact.getName()}"
//
//    Artifactory artifactory = ArtifactoryClientBuilder.create()
//        .setUrl("${atifactory_ip}/artifactory/")
//        .setUsername("${ARTIFACTORY_USER}")
//        .setPassword("${ARTIFACTORY_PWD}")
//        .build()
//
//File result = artifactory.repository("${repository}").upload("${ArtifactoryUploadPath}", artifact).doUpload()
//
//return result.getDownloadUri()
}