package com.epam

@Grab('com.bettercloud:vault-java-driver:3.0.0')
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.Vault

def get_credentials() {

    def config = new VaultConfig()
            .address("http://192.168.56.21:8200")
            .token("${VAULT_TOKEN}")
            .build();

    def vault = new Vault(config);

    def value = vault.logical()
            .read("secret/hello")
            .getData().get("value");

    return value;
}
