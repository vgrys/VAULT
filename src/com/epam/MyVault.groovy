package com.epam

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse

@Grapes(
    @Grab('com.bettercloud:vault-java-driver:3.0.0')
)

static def get_credentials(IP, token, Backend) {

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

    def value = vault.logical().read(Backend).getData().get("username")
    def value2 = vault.logical().read(Backend).getData().get("password")

    return value2



}
