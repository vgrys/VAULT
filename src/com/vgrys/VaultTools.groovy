#!/usr/bin/groovy
//package ctc.ad.corp.cicd

package com.vgrys

@Grapes([
        @Grab('com.bettercloud:vault-java-driver:3.0.0'),
        @GrabExclude(group='org.codehaus.groovy', module='groovy-xml'),
        @GrabExclude(group='xerces', module='xercesImpl')
])

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse

static def populate_credentials(env, vault_ip, token, String environment, String service) {

    final VaultConfig config = new VaultConfig()
            .address(vault_ip)
            .token(token)
            .build()

    final Vault vault = new Vault(config)

    final LogicalResponse response = vault.logical().read("secret/$environment/$service")
    final String username = response.getData().get("username")
    final String password = response.getData().get("password")

    env.setProperty("${service.toUpperCase()}_USER", username)
    env.setProperty("${service.toUpperCase()}_PWD", password)
}
