#!/usr/bin/groovy

@Library('vaultCommands@master')
import com.epam.VaultTools

//Import ctc.ad.corp.cicd.VaultTools   // to be added to Jenkinsfile oin CTC side
def jobBaseName = "${env.JOB_NAME}".split('/')
def ArtifactoryLocalPath = "${JENKINS_HOME}/jobs/${jobBaseName[0]}/branches/${BRANCH_NAME}/builds/${BUILD_NUMBER}/archive/*"
def ArtifactoryUploadPath = '${JOB_NAME}/${BUILD_NUMBER}/'
def ArtifactoryServer
def ArtifactoryRepository = 'test_project'
def ArtifactoryAddress = "http://192.168.56.21:8081/artifactory/${ArtifactoryRepository}"
def uploadSpec = """{
  "files": [
    {
      "pattern": "${ArtifactoryLocalPath}",
      "target": "${ArtifactoryUploadPath}"
    }
 ]
}"""

node {

    stage('Clean Workspace and Check out Source') {
        echo "********** Clean Jenkins workspace and Check out Source ***********"
        deleteDir()
        checkout scm
        echo "********** End of clean Jenkins workspace and Check out Source ***********"
    }

    stage('Obtain credentials from Vault') {
        echo "********* Start to populate secrets from Vault **********"
        def environment = 'dev'
        def vault_ip = 'http://192.168.56.21:8200'
        withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'MY_VAULT_TOKEN')]) {

            def vaultTools = new VaultTools()
            ['sql', 'consul', 'sonarqube', 'artifactory', 'server_dev'].each { service ->
                vaultTools.populate_credentials(env, vault_ip, "$MY_VAULT_TOKEN", environment, service)
            }
        }
        echo "********* Secrets are saved into environment variables **********"
    }

    stage ('Artifactory Configuration') {
        echo "********* Start Artifactory Configuration **********"
        ArtifactoryServer = Artifactory.newServer(ArtifactoryAddress, "${env.ARTIFACTORY_USER}", "${env.ARTIFACTORY_PWD}")
        echo "********* End of Artifactory Configuration **********"
    }

    stage ('Archive Artifacts') {
        echo "********* Archive artifacts **********"
        echo BUILD_ID
        zip archive: true, zipFile: "${jobBaseName[0]}-ts.zip", dir: ''
        archiveArtifacts artifacts: "${jobBaseName[0]}-ts.zip", fingerprint: true, allowEmptyArchive: false, onlyIfSuccessful: true
        echo "********* End of archive artifacts **********"
    }

    stage ('Upload to Artifactory repository') {
        echo "********* Upload artifacts to Artifactory server repository **********"
        script {
            def buildInfo = Artifactory.newBuildInfo()
            buildInfo.env.capture = true
            ArtifactoryServer.upload(uploadSpec)
            echo "********* End of upload artifacts to Artifactory server repository **********"
        }
    }

    //    stage('check env') {
//        echo "********* This step is just for demo **********"
//        echo "SQL_USER is = ${env.SQL_USER}"
//        echo "SQL_PWD is = ${env.SQL_PWD}"
//        echo "CONSUL_USER is = ${env.CONSUL_USER}"
//        echo "CONSUL_PWD is = ${env.CONSUL_PWD}"
//        echo "ATRIFACTORY_USER is = ${env.ARTIFACTORY_USER}"
//        echo "ATRIFACTORY_PWD is = ${env.ARTIFACTORY_PWD}"
//        echo "SONARQUBE_USER is = ${env.SONARQUBE_USER}"
//        echo "SONARQUBE_PWD is = ${env.SONARQUBE_PWD}"
//        echo "SERVER_DEV_USER is = ${env.SERVER_DEV_USER}"
//        echo "SERVER_DEV_PWD is = ${env.SERVER_DEV_PWD}"
//        echo "********* End of step is just for demo **********"
//    }
}