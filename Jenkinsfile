#!/usr/bin/groovy

@Library('shared-library@dev')
//import com.epam.VaultTools
import com.epam.ArtifactoryTools
import com.epam.ZipTools

def bundlePath

node {

    stage('Clean Workspace and Check out Source') {
        echo "********** Clean Jenkins workspace and Check out Source ***********"
        deleteDir()
        checkout scm
        echo "********** End of clean Jenkins workspace and Check out Source ***********"
    }

    stage ('Check branch') {
        echo "********* Start to check actual branch **********"
        if (env.BRANCH_NAME == 'master' || env.BRANCH_NAME == 'dev') {
            echo "Noting to merge. Actual branch is Master or Dev"
        } else {
                echo "Merging "(env.BRANCH_NAME)" to Existing branch"
                sh "pwd"
            }
        echo "********* End of check actual branch **********"
    }

    stage ('tests') {
        echo "********* Start to perform unittest2 **********"
        sh "py.test --junitxml reports/results.xml atf/tests/*.py"
//        sh "nose2 --verbose -c nose2.cfg"
        junit 'reports/**'
        echo "********* End of unittest2 **********"
    }

    stage ('Build ATF project') {
        echo "********* Start to build ATF project **********"
        sh "chmod +x ${WORKSPACE}/build-atf.sh"
        sh "${WORKSPACE}/build-atf.sh"
        echo "********* End of build ATF project **********"
    }

    stage('Create project archive') {
        echo "********* Start to create project archive **********"
        def zip = new ZipTools()
//        ['**/*.py', '**/*.sh'].each { includes ->
//            ['**/*.groovy', '**/tests/*', '**/*__init__*'].each { excludes ->
                bundlePath = zip.bundle(env)
//            }
//        }
        echo "created an archive $bundlePath"
        echo "********* End of create project archive **********"
    }

    stage('Upload artifacts to Artifactory server') {
        echo "********* Start to upload artifacts to Artifactory server **********"
        withCredentials([usernamePassword(credentialsId: 'arifactoryID', usernameVariable: 'ARTIFACTORY_USER', passwordVariable: 'ARTIFACTORY_PWD')]) {
            def repository = 'bigdata-dss-automation'
            def atifactory_ip = 'http://192.168.56.21:8081'
            def artifactory = new ArtifactoryTools()
//            ["${bundlePath}", , 'sonarqube', 'artifactory', 'server_dev'].each { service ->
            def url = artifactory.upload(env, atifactory_ip, repository, "${bundlePath}", "${ARTIFACTORY_USER}", "${ARTIFACTORY_PWD}")
//            def url = artifactory.upload(env, atifactory_ip, repository, "${bundlePath}", "${ARTIFACTORY_USER}", "${ARTIFACTORY_PWD}")
            echo "uploaded an artifact to $url"
        }
        echo "********* End of upload artifacts to Artifactory server **********"
    }

    stage ('Clean up WORKSPACE') {
//            step([$class: 'WsCleanup'])
    }
}