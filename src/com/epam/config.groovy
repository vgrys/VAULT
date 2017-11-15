#!/usr/bin/groovy

package com.epam

LinkedHashMap<String, String> initConfig() {

    return [
            jenkinsSlave1: 'flex1',
            jenkinsSlave2: 'flex',
            sshKeyId     : 'SSH-KEY',
            artifactoryId: 'arifactoryID'
    ]
}