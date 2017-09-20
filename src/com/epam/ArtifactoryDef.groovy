#!/usr/bin/groovy
package com.epam

static def configure_artifactory(env, atifactory_ip, repository) {
    def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
    def jobBaseName = "${env.JOB_NAME}".split('/')
    def projectName = "${jobBaseName[0]}"
    def ArtifactoryUploadPath = "${env.JOB_NAME}/${env.BUILD_NUMBER}/"
    def ArtifactoryAddress = "${atifactory_ip}/artifactory/${repository}"

    def uploadSpec = """{
  "files": [
    {
      "pattern": "*.zip",
      "target": "${ArtifactoryUploadPath}"
    }
 ]
}"""

//    env.setProperty(TIMESTAMP)
    env.setProperty("${"TIMESTAMP".toUpperCase()}", TIMESTAMP)
//    env.setProperty(jobBaseName)
    env.setProperty("${projectName.toUpperCase()}", projectName)
//    env.setProperty(ArtifactoryAddress)
    env.setProperty("${ArtifactoryAddress.toUpperCase()}", ArtifactoryAddress)
//    env.setProperty(uploadSpec)
    env.setProperty("${uploadSpec.toUpperCase()}", uploadSpec)
}
