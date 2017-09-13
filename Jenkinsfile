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

//            def creds = vc.populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN")
            new MyVault().populate_credentials(env ,"http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "consul")
//            vc.populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "sonarqube")
//            vc.populate_credentials("http://192.168.56.21:8200", "$MY_VAULT_TOKEN", ENVIRONMENT, "artifactory")
//            println("${CONSUL_USER}")
//            println(build.getEnvironment(listener).get("CONSUL_USER"))
//            def myVar = build.getEnvironment(listener).get("BUILD_ID")
//            echo(myVar)
        }
    }
    stage('check env') {
//        print (env)
        echo "USER_C is = ${env.USER_C}"
        echo "PWD_C is = ${env.PWD_C}"
    }
}