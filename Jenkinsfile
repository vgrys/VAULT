#!/usr/bin/groovy
//@Library('github.com/vgrys/VAULT@master') _
@Library('vaultCommands@master') import com.epam.MyVault
//import com.epam.pipeline.vaultCommands.vaultCommands

node {
    echo "start"
    def vc = new MyVault()

    def token = withCredentials([string(credentialsId: '$VAULT_TOKEN', variable: 'TOKEN')])



    def creds = vc.get_credentials(token)
    println (creds)

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
