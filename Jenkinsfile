#!/usr/bin/groovy

@Library('vaultCommands@master')
import com.epam.VaultTools


node {
    stage('check env if exist') {
        echo "CONSUL_USER is = ${env.CONSUL_USER}"
        echo "CONSUL_PWD is = ${env.CONSUL_PWD}"
        echo "ATRIFACTORY_USER is = ${env.ARTIFACTORY_USER}"
        echo "ATRIFACTORY_PWD is = ${env.ARTIFACTORY_PWD}"
        echo "SONARQUBE_USER is = ${env.SONARQUBE_USER}"
        echo "SONARQUBE_PWD is = ${env.SONARQUBE_PWD}"
    }
    stage('Obtain credentials from Vault') {
        echo "********** Start to populate secrets from Vault ***********"
        def ENVIRONMENT = "production"
        withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'MY_VAULT_TOKEN')]) {

            def vaultTools = new VaultTools()
            vaultTools.populate_credentials(env, "http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "consul")
            vaultTools.populate_credentials(env, "http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "sonarqube")
            vaultTools.populate_credentials(env, "http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "artifactory")
        }
        echo "********** Secrets are saved into environment variables ***********"
    }
    stage('check env') {
        echo "CONSUL_USER is = ${env.CONSUL_USER}"
        echo "CONSUL_PWD is = ${env.CONSUL_PWD}"
        echo "ATRIFACTORY_USER is = ${env.ARTIFACTORY_USER}"
        echo "ATRIFACTORY_PWD is = ${env.ARTIFACTORY_PWD}"
        echo "SONARQUBE_USER is = ${env.SONARQUBE_USER}"
        echo "SONARQUBE_PWD is = ${env.SONARQUBE_PWD}"
    }

}