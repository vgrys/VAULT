node {
    // define the secrets and the env variables
    def secrets = [
        [$class: 'VaultSecret', path: 'secret/testing', secretValues: [
            [$class: 'VaultSecretValue', envVar: 'testing', vaultKey: 'value_one'],
            [$class: 'VaultSecretValue', envVar: 'testing_again', vaultKey: 'value_two']]],
        [$class: 'VaultSecret', path: 'secret/another_test', secretValues: [
            [$class: 'VaultSecretValue', envVar: 'another_test', vaultKey: 'value']]]
    ]

    // optional configuration, if you do not provide this the next higher configuration
    // (e.g. folder or global) will be used
    def configuration = [$class: 'VaultConfiguration',
                         vaultUrl: 'http://my-very-other-vault-url.com',
                         vaultCredentialId: 'my-vault-cred-id']
    // inside this block your credentials will be available as env variables
    wrap([$class: 'VaultBuildWrapper', configuration: configuration, vaultSecrets: secrets]) {
        sh 'echo $testing'
        sh 'echo $testing_again'
        sh 'echo $another_test'
    }
}
