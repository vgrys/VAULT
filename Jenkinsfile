#!/usr/bin/groovy

@Library('shared-library@release/version1')

        configurationLoaded = false

node() {
    deleteDir()
    checkout scm
    gitInfo()

    def JenkinsfileExt = 'test'
    exist = fileExists "Jenkinsfile.${JenkinsfileExt}"
    if (env.BRANCH_NAME == 'release/version1') {
        echo pipelineConfig.pad("Loading file: 'Jenkinsfile.${JenkinsfileExt}'")
        load("Jenkinsfile.${JenkinsfileExt}")
        configurationLoaded = true
    }

    exist = fileExists "Jenkinsfile.${env.GIT_BRANCH_TYPE}"
    if (env.GIT_BRANCH_TYPE in ['develop', 'master', 'release', 'feature'] && exist) {
        echo pipelineConfig.pad("Loading file: 'Jenkinsfile.${env.GIT_BRANCH_TYPE}'")
        load("Jenkinsfile.${env.GIT_BRANCH_TYPE}")
        configurationLoaded = true

    } else {
        deleteDir()
        error("Configuration file was't loaded.")
    }
}