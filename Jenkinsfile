#!/usr/bin/groovy

configurationLoaded = false

node {
    scmVars = checkout(scm)
    gitInfo()

    if (env.GIT_BRANCH_TYPE in ['develop', 'master', 'release', 'feature']) {
        pipelineType = inferPipelineType("Jenkinsfile.${env.GIT_BRANCH_TYPE}") //figures out if it's release/feature
//        switchPipeline(pipelineType)
//        load("Jenkinsfile.${stageName}")
        configurationLoaded = true
    } else {
        pipelineType = inferPipelineType("Jenkinsfile.default")
        configurationLoaded = true
    }


}

//
//node {
//    scmVars = checkout(scm)
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
//    if (branchName ==~ /^origin\/${stageName}(\/.*)?$/ && exists) {
//        load("Jenkinsfile.${stageName}")
//        configurationLoaded = true
//    }
//}