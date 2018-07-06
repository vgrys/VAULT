#!/usr/bin/groovy

@Library('shared-library@release/version1')


configurationLoaded = false

node() {

    deleteDir()
    checkout scm
    gitInfo()

    // If you need to load Jenkinsfile by branch, use pipelineConfig.discoverJenkinsfile()
    // If you need to load specific Jenkinsfile, use pipelineConfig.discoverJenkinsfile('<extension>)'.
    // Add extension of Jenkinsfile. If you have Jenkinsfile.test add 'pipelineConfig.discoverJenkinsfile('test')'
    def effectiveJenkinsFile = pipelineConfig.discoverJenkinsfile('feature')
    load(effectiveJenkinsFile)
    configurationLoaded = true
}