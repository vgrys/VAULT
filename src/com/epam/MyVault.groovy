#!/usr/bin/groovy
package com.epam

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse
import jenkins.model.Jenkins
import hudson.model.*

@Grapes(
        @Grab('com.bettercloud:vault-java-driver:3.0.0')
)

static def populate_credentials(env ,ip, token, String environment, String service) {

    env.USER_C="hello1"
    env.PAWD_C="hello2"

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
     def res1  = myVault.set_env("${service.toUpperCase()}_USER", username)
    def res2 =  myVault.set_env("${service.toUpperCase()}_PWD", password)
//    return "res1: =" + res1 + "res2: " + res2
}

//def set_env(key, value) {
////    Jenkins.get
////    build.getEnvironment(listener).put(key, value)
////    return "ok"
//    nodes = Jenkins.getInstance().getGlobalNodeProperties()
//    nodes.getAll(hudson.slaves.EnvironmentVariablesNodeProperty.class)
//
//    if (nodes.size() == 1) {
//        envVars = nodes.get(0).getEnvVars()
//        envVars.put(key, value)
//        Jenkins.getInstance().save()
//
//    }
//    return key + " " + value

}

