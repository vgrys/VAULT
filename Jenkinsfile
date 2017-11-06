#!/usr/bin/groovy
@Library('shared-library@master')
import com.epam.ArtifactoryToolsPlugin

//import com.epam.ZipTools
String artifactoryRepo = 'bigdata-dss-automation'
String artifactoryUrl = 'http://192.168.56.105:8081'
String atfVersion = '0.0.1'
String projectName = 'framework'
String projectVersion = '0.1'

//('flex1')
node {

    echo "DEBUG CODE -----> Running ${env.JOB_NAME} on ${env.JENKINS_URL} for branch ${env.BRANCH_NAME}"

    stage('Clean Workspace and Check out Source') {
        echo "********** Clean Jenkins workspace and Check out Source ***********"
        deleteDir()
        checkout scm
        gitInfo()
        echo "********** testing GIT env ***********"
        echo GIT_ORIGIN
        echo env.GIT_USER
        echo env.GIT_PROJECT
        echo env.GIT_REPO
        echo env.GIT_BRANCH
        echo env.JENKIS_SLVALE1
        echo "********** End of testing GIT env ***********"

        echo "********** End of clean Jenkins workspace and Check out Source ***********"
    }

    stage('Create Ansible archive') {
        echo "********* Start to create Ansible archive **********"
        GString sourceFolder = "${WORKSPACE}/ansible/"
        def zip = new ZipTools()
        def bundlePath = zip.bundle(env, sourceFolder, [".git"])
        echo "created an archive $bundlePath"
        echo "********* Ansible archive created **********"
    }

//    stage('Create project archive') {
//        echo "********* Start to create project archive **********"
//        GString sourceFolder = "${WORKSPACE}"
//        def zip = new ZipTools()
//        def bundlePath = zip.bundle(env, sourceFolder, [".git", "test*.py", "File1", ".excludes.txt", '__init__*'])
//        echo "created an archive $bundlePath"
//        echo "********* End of create project archive **********"
//    }

    stage("Install requirements") {
        echo "********* Start to install requirements **********"
        sh "virtualenv --python=/usr/bin/python3.6 --no-site-packages . && . ./bin/activate && ./bin/pip3.6 install -r ${WORKSPACE}/requirements.txt"
        echo "********* End of install requirements **********"
    }

    stage('tests') {
        echo "********* Start to perform unittest2 **********"
        sh "virtualenv --python=/usr/bin/python3.6 --no-site-packages . && . ./bin/activate && py.test --ignore=bin/ --ignore=lib/ --junitxml reports/results.xml "
//        sh "nose2 --verbose -c nose2.cfg"
        junit 'reports/**'
        echo "********* End of unittest2 **********"
    }

    stage('Build ATF project') {
        echo "********* Start to build ATF project **********"
        if (env.BRANCH_NAME == 'master') {
            echo "Branch name is ${env.BRANCH_NAME}, build ATF project "
            sh "chmod +x ${WORKSPACE}/build-atf.sh && ${WORKSPACE}/build-atf.sh"
        } else {
            echo "Branch name is ${env.BRANCH_NAME}, skip build ATF project "
            echo "********* End of build ATF project **********"
        }
    }

    stage('Upload artifacts to Artifactory server') {
        echo "********* Start to upload artifacts to Artifactory server **********"

        GString atfArchivePath = "${WORKSPACE}/dist/*.tar.gz"
        GString projectArchivePath = "${WORKSPACE}/*.tgz"
        def artifactoryServer = Artifactory.newServer url: "${artifactoryUrl}", credentialsId: 'arifactoryID'
        def artifactory = new ArtifactoryToolsPlugin()
        artifactory.artifactoryConfig(env, artifactoryRepo, "${atfArchivePath}", "${projectArchivePath}", atfVersion, projectName, projectVersion)
        artifactoryServer.upload(env.uploadSpec)
        echo "********* End of upload artifacts to Artifactory server **********"
    }

//    stage('playbook test stage') {
//        echo "********* playbook test stage starting **********"
//        dir("${WORKSPACE}/ansible") {
//            sh "ssh -o ServerAliveInterval=30 vagrant@192.168.56.21 uname -a"
//            sh "ansible prod -m ping -u vagrant"
//            sh "ansible-playbook --extra-vars 'server=prod artifactoryUrl=${artifactoryUrl} artifactoryRepo=${artifactoryRepo} projectVersion=${projectVersion} projectName=${projectName} workspace=${WORKSPACE}' projectDeployment.yml"
//        }
//        echo "********* playbook test stage end **********"
//    }

    stage('ATF install') {
        echo "********* Start to install AFT project **********"
        withCredentials([file(credentialsId: 'zeph', variable: 'zephCred')]) {
            dir("${WORKSPACE}/ansible") {
                sh "ansible-playbook --extra-vars 'server=prod artifactoryRepo=${artifactoryRepo} artifactoryUrl=${artifactoryUrl} atfVersion=${atfVersion} workspace=${WORKSPACE} zephCred=${zephCred}' ATFDeployment.yml"
            }
        }
        echo "********* End of install AFT project **********"
    }

    stage('Project deployment') {
        echo "********* Start project deployment **********"
        dir("${WORKSPACE}/ansible") {
            sh "ansible-playbook --extra-vars 'server=prod artifactoryUrl=${artifactoryUrl} artifactoryRepo=${artifactoryRepo} projectVersion=${projectVersion} projectName=${projectName} workspace=${WORKSPACE}' projectDeployment.yml"
        }
        echo "********* End of project deployment **********"
    }

    stage('Clean up WORKSPACE') {
        echo "********* Start to clean up WORKSPACE **********"
//            step([$class: 'WsCleanup'])
        echo "********* Start to clean up WORKSPACE **********"
    }
}