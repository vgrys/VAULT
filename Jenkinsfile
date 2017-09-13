#!/usr/bin/groovy

@Library('vaultCommands@master')
import com.epam.MyVault


node {
    stage('Obtain credentials from Vault') {

        def ENVIRONMENT = "production"

        withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'MY_VAULT_TOKEN')]) {

//            def creds = vc.populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN")
            def creds = new MyVault().populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "consul")
//            vc.populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "sonarqube")
//            vc.populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "artifactory")
            def myVar = build.getEnvironment(listener).get("CONSUL_USER")
            echo(myVar)
        }
    }
//    stage('check env') {
//        println($CONSUL_USER)
//    }
}



