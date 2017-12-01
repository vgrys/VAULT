#!/usr/bin/groovy

@Library('shared-library@release/version1')

        configurationLoaded = false

node() {
    deleteDir()
    checkout scm
    gitInfo()



    String JenkinsfileExt = 'test'
    exist = fileExists "Jenkinsfile.${JenkinsfileExt}"
    if (env.BRANCH_NAME == 'bla-bla/test' && exist) {
        echo pipelineConfig.pad("Loading file: 'Jenkinsfile.${JenkinsfileExt}'")
        load("Jenkinsfile.${JenkinsfileExt}")
        configurationLoaded = true
        deleteDir()
        return true
    }

    exist = fileExists "Jenkinsfile.${env.GIT_BRANCH_TYPE}"
    if (env.GIT_BRANCH_TYPE in ['develop', 'master', 'release', 'feature'] && exist) {
        echo pipelineConfig.pad("Loading file: 'Jenkinsfile.${env.GIT_BRANCH_TYPE}'")
        load("Jenkinsfile.${env.GIT_BRANCH_TYPE}")
        configurationLoaded = true
        deleteDir()
        return true

    } else {
        deleteDir()
        error("Jenkinsfile for '${env.GIT_BRANCH_TYPE}' banch was't loaded.")
    }
}