package com.epam

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse

@Grapes(
    @Grab('com.bettercloud:vault-java-driver:3.0.0')
)

static def get_credentials(IP, token) {

    def config = new VaultConfig()
            .address(IP)
            .token(token)
            .build()

    //def vault = new Vault(config)
        final LogicalResponse response = config.logical().read("secret/consul")
        final username = response.getData().get("username")
        final password = response.getData().get("password")
//      return (username, password)
      return password


//    def value = vault.logical()
//            .read("secret/consul")
//            .getData().get("username")
//
//        return username;
}
