#!/usr/bin/groovy

@Library('vaultCommands@master')
import com.epam.VaultTools


node {
    stage('Obtain credentials from Vault') {

        def ENVIRONMENT = "production"

        withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'MY_VAULT_TOKEN')]) {

            def vaultTools = new VaultTools()
            def res = vaultTools.populate_credentials(env, "http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "consul")
            def res2 = vaultTools.populate_credentials(env, "http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "sonarqube")
            def res3 = vaultTools.populate_credentials(env, "http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "artifactory")

            println(res)
            println(res2)
            println(res3)
        }
    }
    stage('check env') {
        echo "USER is = ${env.CONSUL_USER}"
        echo "PWD is = ${env.CONSUL_PWD}"
        echo "USER is = ${env.ATRIFACTORY_USER}"
        echo "PWD is = ${env.ATRIFACTORY_PWD}"
        echo "USER is = ${env.SONARQUBE_USER}"
        echo "PWD is = ${env.SONARQUBE_PWD}"
    }

}