#!/usr/bin/groovy
//@Library('github.com/vgrys/VAULT@master') _
@Library('vaultCommands@master') import com.epam.MyVault
//import com.epam.pipeline.vaultCommands.vaultCommands

node {
    echo "start"
    def vc = new MyVault()

//    withCredentials([string(credentialsId: '$VAULT_TOKEN', variable: 'TOKEN')])

//    def token = "${env.VAULT_TOKEN}"
 //   print(token)
    withCredentials([string(credentialsId: 'VAULT_TOKEN', Variable: 'MY_VAULT_TOKEN')]) {
//        def env = System.getenv()
//        env.each{
//            println it
//        }
        println "------------------------------------------------"
        println "$MY_VAULT_TOKEN"
        println "------------------------------------------------"
        sh 'echo $MY_VAULT_TOKEN'
//        def creds = vc.get_credentials("$MY_VAULT_TOKEN")
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
