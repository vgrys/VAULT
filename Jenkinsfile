#!/usr/bin/groovy

@Library('shared-library@release/version1')

        configurationLoaded = false

node() {
    deleteDir()
    checkout scm
    gitInfo()

    exist1 = fileExists "Jenkinsfile.${env.GIT_BRANCH_TYPE}"

    String JenkinsfileExt = 'test'
    exist = fileExists "Jenkinsfile.${JenkinsfileExt}"
    if (env.BRANCH_NAME == 'bla-bla/test' && exist) {
        echo pipelineConfig.pad("Loading file: 'Jenkinsfile.${JenkinsfileExt}'")
        load("Jenkinsfile.${JenkinsfileExt}")
        configurationLoaded = true
        return true
    }

    if (env.GIT_BRANCH_TYPE in ['develop', 'master', 'release', 'feature'] && exist1) {
        echo pipelineConfig.pad("Loading file: 'Jenkinsfile.${env.GIT_BRANCH_TYPE}'")
        load("Jenkinsfile.${env.GIT_BRANCH_TYPE}")
        configurationLoaded = true
        return true

    } else {
        deleteDir()
        error("Configuration file 'Jenkinsfile.${env.GIT_BRANCH_TYPE}'was't loaded.")
    }
}