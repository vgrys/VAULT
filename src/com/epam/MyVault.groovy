package com.epam

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse

@Grapes(
        @Grab('com.bettercloud:vault-java-driver:3.0.0')
)

static def populate_credentials(ip, token, String environment, String service) {

    final VaultConfig config = new VaultConfig()
            .address(ip)
            .token(token)
            .build()

    final Vault vault = new Vault(config)

    final LogicalResponse response = vault.logical().read("secret/production/consul")
    final username = response.getData().get('username')
    final password = response.getData().get('password')
    return "user is: $username \nPass is: $password"
//    set_env("${service.toUpperCase()}_USER", username)
//    set_env("${service.toUpperCase()}_PWD", password)
//    return "user is: (\"${service.toUpperCase()}_USER\", username) \npass is: (\"${service.toUpperCase()}_PWD\", password)"

}
