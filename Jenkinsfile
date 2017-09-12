#!/usr/bin/groovy
//@Library('github.com/vgrys/VAULT@master') _
@Library('vaultCommands@master') import com.epam.Vault
//import com.epam.pipeline.vaultCommands.vaultCommands

node {
    echo "start"
    def vc = new Vault()

    def creds = vc.get_credentials()
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
