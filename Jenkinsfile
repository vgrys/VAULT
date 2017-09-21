#!/usr/bin/groovy

@Library('shared-library@dev')
import com.epam.VaultTools
import com.epam.ArtifactoryDef
import com.epam.ZipTools

node {

    stage('Clean Workspace and Check out Source') {
        echo "********** Clean Jenkins workspace and Check out Source ***********"
        deleteDir()
        checkout scm
        echo "********** End of clean Jenkins workspace and Check out Source ***********"
    }

    stage('Obtain credentials from Vault') {
        echo "********* Start to populate secrets from Vault **********"
        def environment_used = 'dev'
        def vault_ip = 'http://192.168.56.21:8200'
        withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'MY_VAULT_TOKEN')]) {

            def vaultTools = new VaultTools()
            ['sql', 'consul', 'sonarqube', 'artifactory', 'server_dev'].each { service ->
                vaultTools.populate_credentials(env, vault_ip, "$MY_VAULT_TOKEN", environment_used, service)
            }
        }
        echo "********* Secrets are saved into environment variables **********"
    }

    stage('Create project archive') {
        echo "********* Start to create project archive **********"
        def archiveProject = new ZipTools()
        archiveProject.create_archive(env)
        echo "********* End of create project archive **********"
    }


    stage('Artifactory configuration') {
        echo "********* Start to configure Artifactory **********"
        def repository = 'bigdata-dss-automation'
        def atifactory_ip = 'http://192.168.56.21:8081'
        def artifactoryDef = new ArtifactoryDef()
//        java.io.File bundle = new java.io.File("${env.WORKSPACE}/TEST.zip")
        def url = artifactoryDef.upload_atrifact(env, atifactory_ip, repository, "${env.WORKSPACE}/TEST.zip")
        echo "uploaded an artifact to $url"
        echo "********* End of configure Artifactory **********"
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