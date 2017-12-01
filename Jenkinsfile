#!/usr/bin/groovy

@Library('shared-library@release/version1')

configurationLoaded = false

node {
    gitInfo()
    exist = fileExists "Jenkinsfile.${env.GIT_BRANCH_TYPE}"

    if (env.GIT_BRANCH_TYPE in ['develop', 'master', 'release', 'feature'] && exist) {
        echo "Loading file: 'Jenkinsfile.${env.GIT_BRANCH_TYPE}'"
        load("Jenkinsfile.${env.GIT_BRANCH_TYPE}")
        configurationLoaded = true
    } else {
        error("Configuration file was't loaded.")
    }
}