#!/usr/bin/groovy
@Grapes(
    @Grab(group='com.bettercloud', module='vault-java-driver', version='3.0.0')
)
final VaultConfig config = new VaultConfig
                                  .address("http://192.168.56.21:8200")
                                  .token("3c9fd6be-7bc2-9d1f-6fb3-cd746c0fc4e8")
                                  .build();

// You may choose not to provide a root token initially, if you plan to use
// the Vault driver to retrieve one programmatically from an auth backend.
final VaultConfig config = new VaultConfig().address("http://192.168.56.21:8200").build();
