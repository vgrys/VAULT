#!/usr/bin/groovy

// src/ctc/ad/corp/cicd/VaultTools.groovy
//package ctc.ad.corp.cicd
package com.epam

@Grapes(
        @Grab('com.bettercloud:vault-java-driver:2.5.2')
)

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse

static def populate_credentials(env, ip_vault, token, String environment, String service) {

    final VaultConfig config = new VaultConfig()
            .address(ip_vault)
            .token(token)
            .build()

    final Vault vault = new Vault(config)

    final LogicalResponse response = vault.logical().read("secret/$environment/$service")
    final String username = response.getData().get("username")
    final String password = response.getData().get("password")

    env.setProperty("${service.toUpperCase()}_USER", username)
    env.setProperty("${service.toUpperCase()}_PWD", password)
}