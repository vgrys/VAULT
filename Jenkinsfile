#!/usr/bin/groovy

@Library('shared-library@release/version1')

configurationLoaded = false

node {
    stage('Clean Workspace and Check out Source') {
        echo "********** Clean Jenkins workspace and Check out Source ***********"
        deleteDir()
        checkout scm
        gitInfo()
        echo "********** End of clean Jenkins workspace and Check out Source ***********"
    }
    exist = fileExists "Jenkinsfile.${env.GIT_BRANCH_TYPE}"

    if (env.GIT_BRANCH_TYPE in ['develop', 'master', 'release', 'feature'] && exist) {
        echo "Loading file: 'Jenkinsfile.${env.GIT_BRANCH_TYPE}'"
        load("Jenkinsfile.${env.GIT_BRANCH_TYPE}")
        configurationLoaded = true
    } else {
        error("Configuration file was't loaded.")
    }
}