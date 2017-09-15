#!/usr/bin/groovy

@Library('vaultCommands@master')
import com.epam.VaultTools

def server = Artifactory.server 'flex1-arti'
echo JOB_NAME
def jobBaseName = "${env.JOB_NAME}".split('/')
def artifactoryLocalLocation = "${JENKINS_HOME}/jobs/${jobBaseName[0]}/branches/${BRANCH_NAME}/builds/${BUILD_NUMBER}/archive/assembly/target/"
def artifactoryUploadPath = 'builds/${BUILD_NUMBER}/'
def uploadSpec = """{
  "files": [
    {
      "pattern": "${artifactoryLocalLocation}",
      "target": "${artifactoryUploadPath}"
    }
 ]
}"""



node {
    stage('Clean Workspace') {
        echo "********** Clean Jenkins workspace ***********"
        deleteDir()
    }

    stage('check env') {
        echo "SQL_USER is = ${env.SQL_USER}"
        echo "SQL_PWD is = ${env.SQL_PWD}"
        echo "CONSUL_USER is = ${env.CONSUL_USER}"
        echo "CONSUL_PWD is = ${env.CONSUL_PWD}"
        echo "ATRIFACTORY_USER is = ${env.ARTIFACTORY_USER}"
        echo "ATRIFACTORY_PWD is = ${env.ARTIFACTORY_PWD}"
        echo "SONARQUBE_USER is = ${env.SONARQUBE_USER}"
        echo "SONARQUBE_PWD is = ${env.SONARQUBE_PWD}"
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

//    stage('Obtain credentials from Vault') {
//        echo "********** Start to populate secrets from Vault ***********"
//        def ENVIRONMENT = "production"
//        withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'MY_VAULT_TOKEN')]) {
//
//            def vaultTools = new VaultTools()
//            vaultTools.populate_credentials(env, "http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "consul")
//            vaultTools.populate_credentials(env, "http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "sonarqube")
//            vaultTools.populate_credentials(env, "http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "artifactory")
//        }
//        echo "********** Secrets are saved into environment variables ***********"
//    }

    stage('check env') {
        echo "SQL_USER is = ${env.SQL_USER}"
        echo "SQL_PWD is = ${env.SQL_PWD}"
        echo "CONSUL_USER is = ${env.CONSUL_USER}"
        echo "CONSUL_PWD is = ${env.CONSUL_PWD}"
        echo "ATRIFACTORY_USER is = ${env.ARTIFACTORY_USER}"
        echo "ATRIFACTORY_PWD is = ${env.ARTIFACTORY_PWD}"
        echo "SONARQUBE_USER is = ${env.SONARQUBE_USER}"
        echo "SONARQUBE_PWD is = ${env.SONARQUBE_PWD}"
    }
}