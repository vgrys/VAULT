package com.epam

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig

@Grapes(
    @Grab('com.bettercloud:vault-java-driver:3.0.0')
)

def get_credentials(IP, token) {

    def config = new VaultConfig()
            .address(IP)
            .token(token)
            .build()

    def vault = new Vault(config)

    def username = vault.logical()
            .read("secret/CREDS")
            .getData().get("username")

    return username;

    def password = vault.logical()
            .read("secret/CREDS")
            .getData().get("password")

    return password;
}
