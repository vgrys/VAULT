#!/usr/bin/groovy
import groovy.transform.NotYetImplemented

class SharedConfiguration implements Serializable {
    def static getDefault() {
        return [
                jenkinsSlave1  : 'flex1',
                jenkinsSlave2  : 'flex',
                sshKeyId       : 'SSH-KEY',
                artifactoryId  : 'arifactoryID',
                artifactoryRepo: 'bigdata-dss-automation',
                artifactoryUrl : 'http://192.168.56.105:8081'
        ]
    }

    def static get(consulURL=null) {
        if (consulURL) {
            throw NotYetImplemented
        }
        return getDefault()
    }
}