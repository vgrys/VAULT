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

    withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'MY_VAULT_TOKEN')]) {
        sh '''
        echo "My KEY1 is: $MY_VAULT_TOKEN"
        '''
    }

    withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'MY_VAULT_TOKEN')]) {
//        def env = System.getenv()
//        env.each{
//            println it
//        }
        sh 'echo $MY_VAULT_TOKEN'
        println "------------------------------------------------"
        println "$MY_VAULT_TOKEN"
        println "------------------------------------------------"

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
