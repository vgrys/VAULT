package com.epam

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse

@Grapes(
    @Grab('com.bettercloud:vault-java-driver:3.0.0')
)

static def get_credentials(IP, token, BE) {

    final VaultConfig config = new VaultConfig()
            .address(IP)
            .token(token)
            .build()

    final Vault vault = new Vault(config)

//        final  LogicalResponse response = vault.logical().read("secret/consul");
//        final String username = response.getData().get("username");
//        final String password = response.getData().get("password");
//        return username
//        return password

    def value = vault.logical().read(BE).getData().get("username")

    return value



}
