#!/usr/bin/groovy

@Library('vaultCommands@master') import com.epam.MyVault

node {
    echo "start"
    def vc = new MyVault()

    withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'MY_VAULT_TOKEN')]) {

        def creds = vc.get_credentials( "http://192.168.56.21:8200", "$MY_VAULT_TOKEN")
        println (creds)
    }
}


//pipeline  {
//  agent any
//  stages {
//    stage('Build Release') {
//        steps{
//            vc.print_command(11)
////            vault {
////            }
//        }
//     }
//    // stage ('check USER') {
//    //     steps {
//    //         printName {
//    //     }
//    //   }
//    // }
//  }
//}
