#!/usr/bin/groovy

@Library('vaultCommands@master')
import com.epam.VaultTools

def server = Artifactory.server 'flex1-arti'
def artifactoryLocalLocation = '/var/lib/cjt/jobs/${JOB_NAME}/branches/${BRANCH_NAME}/builds/${BUILD_NUMBER}/archive/assembly/target/'
def artifactoryUploadPath = 'builds/${BUILD_NUMBER}/'
echo(message: env.JOB_NAME)
def full_name = env.JOB_NAME.split('/')
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
        echo artifactoryLocalLocation
        deleteDir()
        echo "********** End of clean Jenkins workspace ***********"
        echo JOB_NAME
        echo BRANCH_NAME
        echo BUILD_NUMBER
        echo JOB_BASE_NAME
        echo full_name 'full_name'
        echo BUILD_DISPLAY_NAME
        echo BUILD_ID
        echo "********** End of clean Jenkins workspace ***********"
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

//    stage('Results') {
//        junit '**/target/surefire-reports/TEST-*.xml'
//        archive 'target/*.jar'
//    }

    stage ('Archive Artifacts') {
        echo "********* Archive artifacts **********"
        archiveArtifacts 'assembly/target/*.tar.gz'
        fingerprint 'assembly/target/*.tar.gz'
        archiveArtifacts 'assembly/target/*.zip'
        fingerprint 'assembly/target/*.zip'
        echo "********* End of archive artifacts **********"

    }

    stage ('Upload to Artifactory') {
        echo "********* Upload artifacts to Artifactory server **********"
        script {
            def buildInfo = Artifactory.newBuildInfo()
            buildInfo.env.capture = true
            buildInfo=server.upload(uploadSpec)
            server.publishBuildInfo(buildInfo)
            echo "********* End of upload artifacts to Artifactory server **********"
        }
    }
}