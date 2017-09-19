#!/usr/bin/groovy

@Library('vaultCommands@master')
import com.epam.VaultTools

//Import ctc.ad.corp.cicd.VaultTools   // to be added to Jenkinsfile oin CTC side
def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
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
                vaultTools.populate_credentials(environment_variables, vault_ip, "$MY_VAULT_TOKEN", environment, service)
            }
        }
        echo "********* Secrets are saved into environment variables **********"
    }

    stage ('Artifactory Configuration') {
        echo "********* Start Artifactory Configuration **********"
        ArtifactoryServer = Artifactory.newServer(ArtifactoryAddress, "${environment_variables.ARTIFACTORY_USER}", "${environment_variables.ARTIFACTORY_PWD}")
        echo "********* End of Artifactory Configuration **********"
    }

    stage ('Archive Artifacts') {
        echo "********* Archive artifacts **********"
        zip archive: true, zipFile: "${jobBaseName[0]}-${TIMESTAMP}.zip", dir: ''
        archiveArtifacts artifacts: "${jobBaseName[0]}-${TIMESTAMP}.zip", fingerprint: true, allowEmptyArchive: false, onlyIfSuccessful: true
        echo "********* End of archive artifacts **********"
    }

    stage ('Upload to Artifactory repository') {
        echo "********* Upload artifacts to Artifactory server repository **********"
        def buildInfo = Artifactory.newBuildInfo()
        buildInfo.env.capture = true
        ArtifactoryServer.upload(uploadSpec)
        echo "********* End of upload artifacts to Artifactory server repository **********"
    }

        stage('check env') {
        echo "********* This step is just for demo **********"
        echo "SQL_USER is = ${environment_variables.SQL_USER}"
        echo "SQL_PWD is = ${environment_variables.SQL_PWD}"
        echo "CONSUL_USER is = ${environment_variables.CONSUL_USER}"
        echo "CONSUL_PWD is = ${environment_variables.CONSUL_PWD}"
        echo "ATRIFACTORY_USER is = ${environment_variables.ARTIFACTORY_USER}"
        echo "ATRIFACTORY_PWD is = ${environment_variables.ARTIFACTORY_PWD}"
        echo "SONARQUBE_USER is = ${environment_variables.SONARQUBE_USER}"
        echo "SONARQUBE_PWD is = ${environment_variables.SONARQUBE_PWD}"
        echo "SERVER_DEV_USER is = ${environment_variables.SERVER_DEV_USER}"
        echo "SERVER_DEV_PWD is = ${environment_variables.SERVER_DEV_PWD}"
        echo "********* End of step is just for demo **********"
    }
}