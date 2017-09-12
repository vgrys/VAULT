#!/usr/bin/groovy
//@Library('github.com/vgrys/VAULT@master') _
@Library('vaultCommands@master') import com.epam.vaultCommands
//import com.epam.pipeline.vaultCommands.vaultCommands
//def vc = new vaultCommands()
pipeline  {
  agent any
  stages {
    stage('Build Release') {
        steps{
            vaultCommands 11
//            vc.print_command(11)
//            vault {
//            }
        }
     }
    // stage ('check USER') {
    //     steps {
    //         printName {
    //     }
    //   }
    // }
  }
}
