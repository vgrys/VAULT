#!/usr/bin/groovy
@Library('shared-library@release/version1')

String atfVersion = '0.0.1'
String atfRelease = 'release'

String targetGroup = "jenkins"

String playbooksName = 'ci-cd-playbooks'
String playbooksVersion = '0.1'
String bundleName
String nifiURL = 'http://192.168.56.105:8088'
String nifiRootID = 'e3f04b4f-015f-1000-b8f7-a1141fed5991'
String nifiClientID = 'cfc9c5fd-0159-1000-e150-054ac8339ef8'
String sonarUrl = 'http://192.168.56.30:9000'
String sonarToken = 'sonarTokenId'
String sonarQubeScanner = 'SonarQube3.0.3'

def conf = SharedConfiguration.get()

node {
    echo "++++++++++++++++++++++++++ Working on feature branch ++++++++++++++++++++++++++"
    pipelineConfig.beginning()

    stage('Clean Workspace and Check out Source') {
        echo "********** Clean Jenkins workspace and Check out Source ***********"
        deleteDir()
        checkout scm
        gitInfo()
        echo "********** End of clean Jenkins workspace and Check out Source ***********"
    }

    stage('SonarQube analysis') {
        sonar.scanner(sonarUrl, sonarToken, sonarQubeScanner, GIT_REPO, BUILD_ID)
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
        echo "********* Start to upload Ansible to Artifactory server **********"
        artifactoryTools.uploadAnsible(conf.artifactoryUrl, conf.artifactoryRepo, playbooksName, conf.artifactoryId)
        echo "********* End of upload Ansible to Artifactory server **********"
    }

    stage('Upload ATF archive to Artifactory server') {
        echo "********* Start to upload ATF archive to Artifactory server **********"
        artifactoryTools.ATFUpload(conf.artifactoryUrl, conf.artifactoryRepo, conf.artifactoryId)
        echo "********* End of upload ATF archive to Artifactory server **********"
    }

    stage('ATF install') {
        echo "********* Start to install AFT project **********"
        dir("${WORKSPACE}/ansible") {
            sh "ansible-playbook --limit ${targetGroup} --extra-vars 'server=${targetGroup} hostUser=" { conf.targetHostUser }
            " artifactoryRepo=${conf.artifactoryRepo} artifactoryUrl=${conf.artifactoryUrl} atfVersion=${atfVersion} atfRelease=${atfRelease}' ATFDeployment.yml"
        }
        echo "********* End of install AFT project **********"
    }

    stage('Project deployment') {
        echo pipelineConfig.pad("Start project deployment")
        pipelineConfig.runDeployProject(conf.artifactoryUrl, conf.artifactoryRepo, "test-project", "test-project-20171108105623.tgz", targetGroup, conf.artifactoryId)
        echo pipelineConfig.pad("End of project deployment")
    }

    stage('Clean up WORKSPACE') {
        echo "********* Start to clean up WORKSPACE **********"
//            step([$class: 'WsCleanup'])
        echo "********* Start to clean up WORKSPACE **********"
    }
}