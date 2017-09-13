#!/usr/bin/groovy

@Library('vaultCommands@master')
import com.epam.MyVault


node {
    stage('check env') {
//        print (env)
//        echo "USER_C is = ${env.USER_C}"
}
    stage('Obtain credentials from Vault') {

        def ENVIRONMENT = "production"

        withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'MY_VAULT_TOKEN')]) {

           new MyVault().populate_credentials(env ,"http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "consul")

//            new MyVault()vc.populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "sonarqube")
//            new MyVault()vc.populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "artifactory")
//        }
    }
    stage('check env')
        echo "USER_C is = ${env.USER_C}"
        echo "PWD_C is = ${env.PWD_C}"
    }
}