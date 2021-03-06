#!/usr/bin/groovy
import groovy.transform.NotYetImplemented

class SharedConfiguration implements Serializable {
    def static get() {
        return [
                jenkinsSlave1  : 'flex1',
                jenkinsSlave2  : 'flex',
                sshKeyId       : 'jenkins-private-key',
                artifactoryId  : 'artifactoryId',
                artifactoryRepo: 'bigdata-dss-automation',
                artifactoryUrl : 'http://192.168.56.30:8081',
                targetHostUser : 'vagrant',
                targetGroup    : 'jenkins',
                projectName    : 'env.GIT_REPO',
                NiFiVersion    : '1.4.0'
        ]
    }

    def static get(consulURL) {
            throw NotYetImplemented
    }
}