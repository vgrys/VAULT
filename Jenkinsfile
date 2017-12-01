#!/usr/bin/groovy

@Library('shared-library@release/version1')

configurationLoaded = false

node {
    scmVars = checkout(scm)
    gitInfo()
    exist = fileExists "Jenkinsfile.${env.GIT_BRANCH_TYPE}"
    def switchPipeline = ''

    if (env.GIT_BRANCH_TYPE in ['develop', 'master', 'release', 'feature'] && exist) {

//        exists = fileExists "Jenkinsfile.${env.GIT_BRANCH_TYPE}"
        switchPipeline("Jenkinsfile.${env.GIT_BRANCH_TYPE}") //figures out if it's release/feature
//        switchPipeline(pipelineType)
        load("Jenkinsfile.${stageName}")
        configurationLoaded = true
    } else {
        switchPipeline("Jenkinsfile.default")
        configurationLoaded = true
    }


}

//
//node {
//    scmVars = checkout(scm)
//    gitInfo()
//
//    stage("feature") {
//        switchPipeline("${env.STAGE_NAME}", "${scmVars.GIT_BRANCH}")
//    }
//
//    stage("release") {
//        switchPipeline("${env.STAGE_NAME}", "${scmVars.GIT_BRANCH}")
//    }
//
//    stage("default") {
//        if (!configurationLoaded) {
//            error("Configuration file was't loaded.")
//        }
//    }
//}
//
//// It goes into DSS library.
//def switchPipeline(stageName, branchName) {
//    exists = fileExists "Jenkinsfile.${stageName}"
//
//    if (branchName == /^origin\/${stageName}(\/.*)?$/ && exists) {
//        load("Jenkinsfile.${stageName}")
//        configurationLoaded = true
//    }
//}