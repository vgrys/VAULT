#!/usr/bin/groovy

@Library('vaultCommands@master') import com.epam.MyVault


node {
    stage('Obtain credentials from Vault') {
        def vc = new MyVault()
        def ENVIRONMENT = "production"

        withCredentials([string(credentialsId: "VAULT_TOKEN", variable: "MY_VAULT_TOKEN")]) {

            vc.populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN")
//            vc.populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "consul")
//            vc.populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "sonarqube")
//            vc.populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "artifactory")
        }
    }
}



//node {
//    stage('Obtain credentials from Vault') {
//        def vc = new MyVault()
//        def ENVIRONMENT = "production"
//
//        withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'MY_VAULT_TOKEN')]) {
//
//            vc.populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "consul")
////            vc.populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "sonarqube")
////            vc.populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "artifactory")
//        }
//    }
//    stage ('test') {
//        echo "test"
//    }
//}


