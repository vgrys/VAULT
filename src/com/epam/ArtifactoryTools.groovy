package com.epam

@Grapes(
        @Grab('org.jfrog.artifactory.client:artifactory-java-client-api:2.5.2')
)

import org.artifactory.client.Artifactory
import org.artifactory.client.ArtifactoryClient



static def provide_credentials(ArtifactoryUrl, username, password) {

    Artifactory artifactory_ID = ArtifactoryClient.create(ArtifactoryUrl, username, password)
    return artifactory_ID
}
//
//ItemHandle fileItem = artifactory.repository("generic-local ").file("path/to/file.txt");
//ItemHandle folderItem = artifactory.repository("generic-local ").folder("path/to/folder");
//
//
//import com.bettercloud.vault.Vault
//import com.bettercloud.vault.VaultConfig
//import com.bettercloud.vault.response.LogicalResponse
//
//static def populate_credentials(env, ip_vault, token, String environment, String service) {
//
//    final VaultConfig config = new VaultConfig()
//            .address(ip_vault)
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