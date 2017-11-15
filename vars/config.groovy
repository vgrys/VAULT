#!/usr/bin/groovy

// In DSS lib with configs
//def conf = [
def static conf () {
    return [
            jenkinsSlave1: 'flex1',
            jenkinsSlave2: 'flex',
            sshKeyId     : 'SSH-KEY',
            artifactoryId: 'arifactoryID'
    ]
}