#!/usr/bin/groovy

@Library('shared-library@dev')
//import com.epam.VaultTools
import com.epam.ArtifactoryTools
import com.epam.ZipTools

def bundlePath
def isMaster = env.BRANCH_NAME == 'master'
def isDevelop = env.BRANCH_NAME == 'dev'

node {

    stage('Clean Workspace and Check out Source') {
        echo "********** Clean Jenkins workspace and Check out Source ***********"
        deleteDir()
        checkout scm
        echo "********** End of clean Jenkins workspace and Check out Source ***********"
    }



//    stage('Obtain credentials from Vault') {
//        echo "********* Start to populate secrets from Vault **********"
//        def environment_used = 'dev'
//        def vault_ip = 'http://192.168.56.21:8200'
//        withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'MY_VAULT_TOKEN')]) {
//
//            def vaultTools = new VaultTools()
//            ['sql', 'consul', 'sonarqube', 'artifactory', 'server_dev'].each { service ->
//                vaultTools.populate_credentials(env, vault_ip, "$MY_VAULT_TOKEN", environment_used, service)
//            }
//        }
//        echo "********* Secrets are saved into environment variables **********"
//    }

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
        "${WORKSPACE}/build-atf.sh"
        echo "********* End of build ATF project **********"
    }


    stage('Create project archive') {
        echo "********* Start to create project archive **********"
        def zip = new ZipTools()
        ['**/*.py', '**/*.sh'].each { includes ->
            ['**/*.groovy', '**/tests/*', '**/*__init__*'].each { excludes ->
                bundlePath = zip.bundle(env, includes, excludes)
            }
        }
        echo "created an archive $bundlePath"
        echo "********* End of create project archive **********"
    }


    stage('Upload artifacts to Artifactory server') {
        echo "********* Start to upload artifacts to Artifactory server **********"
        withCredentials([usernamePassword(credentialsId: 'arifactoryID', usernameVariable: 'ARTIFACTORY_USER', passwordVariable: 'ARTIFACTORY_PWD')]) {
            def repository = 'bigdata-dss-automation'
            def atifactory_ip = 'http://192.168.56.21:8081'
            files = findFiles(glob: '**/.tar.gz')
//            def atfArtifact = "${WORKSPACE}/dist/*.tar.gz"
            echo files[0].name

            def artifactory = new ArtifactoryTools()
//            ["${bundlePath}", , 'sonarqube', 'artifactory', 'server_dev'].each { service ->
            def url = artifactory.upload(env, atifactory_ip, repository, "${bundlePath}", "${ARTIFACTORY_USER}", "${ARTIFACTORY_PWD}")
//            def url = artifactory.upload(env, atifactory_ip, repository, "${bundlePath}", "${ARTIFACTORY_USER}", "${ARTIFACTORY_PWD}")
            echo "uploaded an artifact to $url"
        }
        echo "********* End of upload artifacts to Artifactory server **********"
    }


    if (isDevelop || isMaster) {
        deployCmd = isMaster ? 'fab deploy_prod' : 'fab deploy_staging'
//        sshagent([sshCredentialsId]) {
            stage(name: 'Deploy') {
//                sh "source ${workspace}/env/bin/activate && ${deployCmd}"
                sh "echo $USER"
                sh "pwd"
//            }
        }
    }

    stage ('Clean up WORKSPACE') {
//            step([$class: 'WsCleanup'])
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