package com.epam

import com.bettercloud.vault.Vault
@Grab('com.bettercloud:vault-java-driver:3.0.0')
import com.bettercloud.vault.VaultConfig

def get_credentials(IP, token) {

    def config = new VaultConfig()
            .address(IP)
            .token(token)
            .build()

    def vault = new Vault(config)

    def value = vault.logical()
            .read("secret/CREDS")
            .getData().get("username", "password")

    return value;
}
