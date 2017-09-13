#!/usr/bin/groovy
package com.epam

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse
import hudson.model.*

@Grapes(
        @Grab('com.bettercloud:vault-java-driver:3.0.0')
)

static def populate_credentials(env ,ip, token, String environment, String service) {

    final VaultConfig config = new VaultConfig()
            .address(ip)
            .token(token)
            .build()

    final Vault vault = new Vault(config)

    final LogicalResponse response = vault.logical().read("secret/$environment/$service")
    final String username = response.getData().get("username")
    final String password = response.getData().get("password")

    def myVault = new com.epam.MyVault()
    myVault.set_env(env,"${service.toUpperCase()}_USER", username)
    myVault.set_env(env,"${service.toUpperCase()}_PWD", password)

}

def set_env(env, key, value) {
    env.USER_C="hello1"
    env.PAWD_C="hello2"
}

