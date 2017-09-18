#!/usr/bin/groovy

@Library('vaultCommands@master')
import com.epam.VaultTools


def jobBaseName = "${env.JOB_NAME}".split('/')
def ArtifactoryLocalLocation = "${JENKINS_HOME}/jobs/${jobBaseName[0]}/branches/${BRANCH_NAME}/builds/${BUILD_NUMBER}/archive/*"
def ArtifactoryUploadPath = '${JOB_NAME}/${BUILD_NUMBER}/'
def ArtifactoryServer
def ArtifactoryRepository = 'test_project'
def ArtifactoryServerURL = "http://192.168.56.21:8081/artifactory/${ArtifactoryRepository}"
def uploadSpec = """{
  "files": [
    {
      "pattern": "${ArtifactoryLocalLocation}",
      "target": "${ArtifactoryUploadPath}"
    }
 ]
}"""

node {

    stage('Clean Workspace') {
        echo "********** Clean Jenkins workspace ***********"
        deleteDir()
        echo "********** End of clean Jenkins workspace ***********"
    }

    stage('Check out Source') {
        echo "********** Checkout SCM  ***********"
        checkout scm
        echo "********** End of checkout SCM  ***********"
    }

    stage('Obtain credentials from Vault') {
        echo "********* Start to populate secrets from Vault **********"
        def environment = 'dev'
        def vault_ip = 'http://192.168.56.21:8200'
        withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'MY_VAULT_TOKEN')]) {

            def vaultTools = new VaultTools()
            ['sql', 'consul', 'sonarqube', 'artifactory'].each { service ->
                vaultTools.populate_credentials(env, vault_ip, "$MY_VAULT_TOKEN", environment, service)
            }
        }
        echo "********* Secrets are saved into environment variables **********"
    }

    stage ('Artifactory Configuration') {
        echo "********* Start Artifactory Configuration **********"
        ArtifactoryServer = Artifactory.newServer(ArtifactoryServerURL, "${env.ARTIFACTORY_USER}", "${env.ARTIFACTORY_PWD}")
        echo "********* End of Artifactory Configuration **********"

    }
    stage('check env') {
        echo "********* This step is just for demo **********"
        echo "SQL_USER is = ${env.SQL_USER}"
        echo "SQL_PWD is = ${env.SQL_PWD}"
        echo "CONSUL_USER is = ${env.CONSUL_USER}"
        echo "CONSUL_PWD is = ${env.CONSUL_PWD}"
        echo "ATRIFACTORY_USER is = ${env.ARTIFACTORY_USER}"
        echo "ATRIFACTORY_PWD is = ${env.ARTIFACTORY_PWD}"
        echo "SONARQUBE_USER is = ${env.SONARQUBE_USER}"
        echo "SONARQUBE_PWD is = ${env.SONARQUBE_PWD}"
        echo "********* End of step is just for demo **********"
    }

    stage ('Archive Artifacts') {
        echo "********* Archive artifacts **********"
//        archiveArtifacts '**/bin/*.py'
//        fingerprint '**/bin/*.py'

        zip archive: true, dir: '', glob: '**/bin/*.py', zipFile: 'Project$_{env.JOB_NAME}.zip'
        def ARCH = archive '**/bin/*.py'
        def stash1 = stash includes: '**, .py/', name: 'source', useDefaultExcludes: false

        echo ARCH
        echo stash1
        echo "********* End of archive artifacts **********"

    }

//    node('docker') {
//        stage "Checkout"
//        deleteDir()
//        checkout scm
//        sh '''bash scripts/generate-docker-base.sh
//          bash scripts/build-docker-base.sh ubuntu-yakkety
//          bash scripts/check-patch.sh'''
//        zip archive: true, dir: '', glob: 'scripts/**', zipFile: 'scripts.zip'
//        archive 'scripts/**'
//        stash includes: '**, .git/', name: 'source', useDefaultExcludes: false
//        slackSend channel: 'jenkins', message: "Build Started - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
//        sh "env"
//    }

    stage ('Upload to Artifactory') {
        echo "********* Upload artifacts to Artifactory server **********"
        script {
            def buildInfo = Artifactory.newBuildInfo()
            buildInfo.env.capture = true
            ArtifactoryServer.upload(uploadSpec)
            echo "********* End of upload artifacts to Artifactory server **********"
        }
    }
}