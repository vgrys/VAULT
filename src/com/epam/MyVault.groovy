#!/usr/bin/groovy
package com.epam

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse

@Grapes(
        @Grab('com.bettercloud:vault-java-driver:3.0.0')
)

//static def populate_credentials(ip, token, String environment, String service) {
static def populate_credentials(ip, token, Backend) {

    final VaultConfig config = new VaultConfig()
            .address(ip)
            .token(token)
            .build()

    final Vault vault = new Vault(config)

        final LogicalResponse response = vault.logical().read(Backend)
        final String username = response.getData().get("username")
        final String password = response.getData().get("password")
        return username
//    return "User is: $username \nPass is: $password"
//    set_env("${service.toUpperCase()}_USER", username)
//    set_env("${service.toUpperCase()}_PWD", password)
//    return "user is: (\"${service.toUpperCase()}_USER\", username) \npass is: (\"${service.toUpperCase()}_PWD\", password)"

//    final  LogicalResponse response = vault.logical().read(Backend)
//    final String username = response.getData().get("username")
//    final String password = response.getData().get("password")

}
