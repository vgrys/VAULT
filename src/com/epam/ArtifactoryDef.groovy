#!/usr/bin/groovy
package com.epam

static def configure_artifactory(env, atifactory_ip, repository) {
    def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
    def jobBaseName = "${env.JOB_NAME}".split('/')
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
    env.setProperty("${TIMESTAMP.toUpperCase()}", TIMESTAMP)
//    env.setProperty(jobBaseName)
    env.setProperty("${jobBaseName.toUpperCase()}", jobBaseName)
//    env.setProperty(ArtifactoryAddress)
    env.setProperty("${ArtifactoryAddress.toUpperCase()}", ArtifactoryAddress)
//    env.setProperty(uploadSpec)
    env.setProperty("${uploadSpec.toUpperCase()}", uploadSpec)



//    zip archive: true, zipFile: "${jobBaseName[0]}-${TIMESTAMP}.zip", dir: ''
}

//static def populate_credentials(env, vault_ip, token, String environment, String service) {
//
//    final VaultConfig config = new VaultConfig()
//            .address(vault_ip)
//            .token(token)
//            .build()
//
//    final Vault vault = new Vault(config)
//
//    final LogicalResponse response = vault.logical().read("secret/$environment/$service")
//    final String username = response.getData().get("username")
//    final String password = response.getData().get("password")
//
//    env.setProperty("${service.toUpperCase()}_USER", username)
//    env.setProperty("${service.toUpperCase()}_PWD", password)
//}