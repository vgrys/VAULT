package com.epam

@Grapes(
        @Grab('org.jfrog.artifactory.client:artifactory-java-client-services:jar:0.16')
)

import org.artifactory.client.Artifactory
import org.artifactory.client.ArtifactoryClient



static def provide_credentials(ArtifactoryUrl, username, password) {

    Artifactory artifactory_ID = ArtifactoryClient.create(ArtifactoryUrl, username, password)
    return artifactory_ID
}


//from pipeline:
//
//        stage('Initialize Artifactory') {
//            echo "********* Start to Initialize Artifactory **********"
//            def artifactoryTools = new ArtifactoryTools()
//            def arts = artifactoryTools.provide_credentials(ArtifactoryServer, "${env.ARTIFACTORY_USER}", "${env.ARTIFACTORY_PWD}")
//            echo arts
//
//            echo "********* Start to Initialize Artifactory **********"
//        }