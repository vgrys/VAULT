#!/usr/bin/groovy
//@Library('github.com/vgrys/VAULT@master') _
@Library('vaultCommands@master')  import com.epam.vaultCommands
//import com.epam.pipeline.vaultCommands.vaultCommands

pipeline  {
  agent any
  stages {
    stage('Build Release') {
        steps{
            def vc = vaultCommands()
            vc.print_command(11)
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
