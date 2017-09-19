package com.epam


def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
def jobBaseName = "${env.JOB_NAME}".split('/')




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