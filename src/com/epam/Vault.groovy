package com.epam

@Grab('com.bettercloud:vault-java-driver:3.0.0')
//import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.Vault
import com.epam.VaultConfig

def get_credentials() {

    def config = new VaultConfig
            .address("http://192.168.56.21:8200")
            .token("32dc6946-75cb-dbd7-e718-77b12be3a74b")
            .build();

    def vault = new Vault(config);

    def value = vault.logical()
            .read("secret/hello")
            .getData().get("value");

    return value;
}
