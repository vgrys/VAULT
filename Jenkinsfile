#!/usr/bin/groovy
//@Library('github.com/vgrys/VAULT@master') _
@Library('vaultCommands') _
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
