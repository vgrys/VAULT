#!/usr/bin/groovy
package com.epam

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse
import hudson.model.*

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

    set_env("${service.toUpperCase()}_USER", username)
    set_env("${service.toUpperCase()}_PWD", password)
//    return "user is: (\"${service.toUpperCase()}_USER\", username) \npass is: (\"${service.toUpperCase()}_PWD\", password)"

}

static def set_env(key, value) {
//    System.setProperty(key, value)

    def build = Thread.currentThread().executable
    def pa = new ParametersAction([
            new StringParameterValue(key, value)
    ])
    build.addAction(pa)
}



