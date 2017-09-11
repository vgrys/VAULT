#!/usr/bin/groovy
@Library('github.com/vgrys/VAULT@master')

pipeline  {
  agent any
  stages {
    stage('Build Release') {
      steps{
        mavenCanaryRelease {
      }
     }
    }
  }
 }
