#!/usr/bin/groovy
@Library('shared-library@release/version1')

String atfVersion = '0.1.0'
String atfRelease = 'release'

String targetGroup = "prod"

String playbooksName = 'ci-cd-playbooks'
String playbooksVersion = '0.1'
String bundleName


def config = SharedConfiguration.getProperty
echo config.(jenkinsSlave1)

def cfg = config.conf
println(cfg.jenkinsSlave1)
println(cfg.targetHostUser)
println(cfg.targetHost)

node {
    pipelineConfig.beginning()

    stage('Clean Workspace and Check out Source') {
        echo "********** Clean Jenkins workspace and Check out Source ***********"
        deleteDir()
        checkout scm
        gitInfo()

        echo "********** testing GIT env ***********"
        echo env.GIT_PROJECT_KEY
        echo GIT_ORIGIN
        echo env.GIT_USER
        echo env.GIT_PROJECT
        echo "env.GIT_REPO is: $env.GIT_REPO"
        echo env.GIT_BRANCH
        echo env.JENKIS_SLVALE1
        echo env.GIT_FEATURE_NAME
        echo currentBuild.result

        echo "********** End of testing GIT env ***********"

        echo "********** End of clean Jenkins workspace and Check out Source ***********"
    }

    stage('Create Ansible archive') {
        echo "********* Start to create Ansible archive **********"
        GString sourceFolder = "${env.WORKSPACE}/ansible"
        if (env.GIT_BRANCH_TYPE in ['develop', 'master', 'release', 'feature']) {
            echo " Create Ansible archive, branch is '${env.GIT_BRANCH_TYPE}'"
            def zip = new ZipTools()
            bundleName = zip.bundle("${sourceFolder}", [".git"], "${playbooksName}-${playbooksVersion}.tgz")
            echo "created an archive '$bundleName'"
            echo "${env.PROJECT_ARCHIVE}"
        } else {
            echo "Branch name is '${env.GIT_BRANCH_TYPE}', skip to create Ansible archive "
        }
        echo "********* End of stage 'Create Ansible archive' **********"
    }

//    stage("Install requirements") {
//        echo "********* Start to install requirements **********"
//        sh "virtualenv --python=/usr/bin/python3.6 --no-site-packages . && . ./bin/activate && ./bin/pip3.6 install -r ${WORKSPACE}/requirements.txt"
//        echo "********* End of install requirements **********"
//    }
//
//    stage('tests') {
//        echo "********* Start to perform unittest2 **********"
//        sh "virtualenv --python=/usr/bin/python3.6 --no-site-packages . && . ./bin/activate && py.test --ignore=bin/ --ignore=lib/ --junitxml reports/results.xml "
////        sh "nose2 --verbose -c nose2.cfg"
//        junit 'reports/**'
//        echo "********* End of unittest2 **********"
//    }

    stage('Build ATF project') {
        echo "${env.PROJECT_ARCHIVE}"

        echo "********* Start to build ATF project **********"
        if (env.GIT_BRANCH_TYPE in ['develop', 'master', 'release', 'feature']) {
            echo " Build ATF project because branch is '${env.GIT_BRANCH_TYPE}'"
            sh "chmod +x ${WORKSPACE}/build-atf.sh && ${WORKSPACE}/build-atf.sh"
        } else {
            echo "Branch name is ${env.BRANCH_NAME}, skip build ATF project "
            echo "********* End of build ATF project **********"
        }
    }

    stage('Upload Ansible to Artifactory server') {
        echo "cfg.artifactoryId is: '${cfg.artifactoryId}'"
        echo "********* Start to upload Ansible to Artifactory server **********"
        artifactoryTools.uploadAnsible(cfg.artifactoryUrl, cfg.artifactoryRepo, playbooksName, cfg.artifactoryId)
        echo "********* End of upload Ansible to Artifactory server **********"
    }

    stage('Upload ATF archive to Artifactory server') {
        echo "********* Start to upload ATF archive to Artifactory server **********"
        artifactoryTools.ATFUpload(cfg.artifactoryUrl, cfg.artifactoryRepo, cfg.artifactoryId)
        echo "********* End of upload ATF archive to Artifactory server **********"
    }

    stage('ATF install') {
        echo "********* Start to install AFT project **********"
        withCredentials([file(credentialsId: 'zeph', variable: 'zephCred')]) {
            dir("${WORKSPACE}/ansible") {
                sh "ansible-playbook --limit ${targetGroup} --extra-vars 'server=${targetGroup} artifactoryRepo=${cfg.artifactoryRepo} artifactoryUrl=${cfg.artifactoryUrl} atfVersion=${atfVersion} atfRelease=${atfRelease} zephCred=${zephCred}' ATFDeployment.yml"
            }
        }
        echo "********* End of install AFT project **********"
    }

    stage('Project deployment') {
        echo pipelineConfig.pad("Start project deployment")
        pipelineConfig.runDeployProject(cfg.artifactoryUrl, cfg.artifactoryRepo, "test-project", "test-project-20171108105623.tgz"  ,targetGroup, cfg.artifactoryId)
        echo pipelineConfig.pad("End of project deployment")
    }

    stage('Clean up WORKSPACE') {
        echo "********* Start to clean up WORKSPACE **********"
//            step([$class: 'WsCleanup'])
        echo "********* Start to clean up WORKSPACE **********"
    }
    echo "$currentBuild.result"
}