#!/usr/bin/groovy

class config implements Serializable {

    def conf = [
            jenkinsSlave1: 'flex1',
            jenkinsSlave2: 'flex',
            sshKeyId     : 'SSH-KEY',
            artifactoryId: 'arifactoryID'
    ]
}