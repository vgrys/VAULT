#!/usr/bin/groovy

class SharedConfiguration implements Serializable {
    def get() {
        return [
                jenkinsSlave1  : 'flex1',
                jenkinsSlave2  : 'flex',
                sshKeyId       : 'SSH-KEY',
                artifactoryId  : 'arifactoryID',
                artifactoryRepo: 'bigdata-dss-automation',
                artifactoryUrl : 'http://192.168.56.105:8081'
        ]
    }

    def get(consulURL) {
        return "not yet implemented"
    }
}