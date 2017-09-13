#!/usr/bin/groovy
package com.epam

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse
import hudson.slaves.EnvironmentVariablesNodeProperty
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
    myVault.set_env("${service.toUpperCase()}_USER", username)
    myVault.set_env("${service.toUpperCase()}_PWD", password)

}

def set_env(key, value) {


    instance = Jenkins.getInstance()
    globalNodeProperties = instance.getGlobalNodeProperties()
    envVarsNodePropertyList = globalNodeProperties.getAll(EnvironmentVariablesNodeProperty.class)

    newEnvVarsNodeProperty = null
    envVars = null

    if (envVarsNodePropertyList == null || envVarsNodePropertyList.size() == 0) {
        newEnvVarsNodeProperty = new EnvironmentVariablesNodeProperty();
        globalNodeProperties.add(newEnvVarsNodeProperty)
        envVars = newEnvVarsNodeProperty.getEnvVars()
    } else {
        envVars = envVarsNodePropertyList.get(0).getEnvVars()
    }

    envVars.put(key, value)

    instance.save()
}



