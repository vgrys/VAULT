package com.epam

import com.bettercloud.vault.Vault
@Grab('com.bettercloud:vault-java-driver:3.0.0')
import com.bettercloud.vault.VaultConfig

def get_credentials(token) {

    print(token)
    def config = new VaultConfig()
            .address("http://192.168.56.21:8200")
            .token(token)
            .build();

    def vault = new Vault(config);

    def value = vault.logical()
            .read("secret/hello")
            .getData().get("value");

    return value1;
}
