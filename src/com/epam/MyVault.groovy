#!/usr/bin/groovy
package com.epam

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse
import jenkins.model.Jenkins

@Grapes(
        @Grab('com.bettercloud:vault-java-driver:3.0.0')
)

static def populate_credentials(ip, token, String environment, String service) {

    final VaultConfig config = new VaultConfig()
            .address(ip)
            .token(token)
            .build()

    final Vault vault = new Vault(config)

    final LogicalResponse response = vault.logical().read("secret/$environment/$service")
    final String username = response.getData().get("username")
    final String password = response.getData().get("password")
//    return "user is: $username \npass is: $password"

    def myVault = new com.epam.MyVault()
    def result3 = myVault.set_env("${service.toUpperCase()}_USER", username)
    def result2 = myVault.set_env("${service.toUpperCase()}_PWD", password)
    return result2 + "|||" + result3
}

def set_env(key, value) {
    nodes = Jenkins.getInstance().getGlobalNodeProperties()
    nodes.getAll(hudson.slaves.EnvironmentVariablesNodeProperty.class)

    if (nodes.size() == 1) {
        envVars = nodes.get(0).getEnvVars()
        envVars.put(key, value)
        Jenkins.getInstance().save()

    }
    return key + " " + value
}


