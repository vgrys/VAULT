#!/usr/bin/groovy

class config implements Serializable {

    def conf = [
            jenkinsSlave1: 'flex1',
            jenkinsSlave2: 'flex',
            sshKeyId     : 'SSH-KEY',
            artifactoryId: 'arifactoryID',
            artifactoryRepo: 'bigdata-dss-automation',
            artifactoryUrl: 'http://192.168.56.105:8081',
            targetHostUser  : 'user',
            targetHost      : "${targetHostUser}@host"

    ]
}