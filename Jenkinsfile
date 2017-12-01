#!/usr/bin/groovy

@Library('shared-library@release/version1')

configurationLoaded = false

node() {
    deleteDir()
    checkout scm
    gitInfo()

    exist = fileExists "Jenkinsfile.${env.GIT_BRANCH_TYPE}"
    if (env.GIT_BRANCH_TYPE in ['develop', 'master', 'release', 'feature'] && exist) {
        try {
            echo pipelineConfig.pad("Loading file: 'Jenkinsfile.${env.GIT_BRANCH_TYPE}'")
            load("Jenkinsfile.${env.GIT_BRANCH_TYPE}")
            configurationLoaded = true
        } finally {
            deleteDir()
        }

    } else {
        deleteDir()
        error("Configuration file was't loaded.")
    }
}