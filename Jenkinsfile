#!groovy
node {
  //
  def vault_server = 'http://192.168.56.21:8200'
  // repository on Artifactory server
  stage ('reading credentials for Vault') {
    withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'VAULT_TOKEN')]) {
      sh '''
      echo "My secret is token: $VAULT_TOKEN"
      '''
     }
  
    withCredentials([string(credentialsId: 'VAULT_KEY1', variable: 'VAULT_KEY1')]) {
      sh '''
      (curl -X PUT -d '{"key": "$VAULT_KEY1"}' http://192.168.56.21:8200/v1/sys/unseal)
      '''
     }
  
    withCredentials([string(credentialsId: 'VAULT_KEY2', variable: 'VAULT_KEY2')]) {
      sh '''
      curl \
          -X PUT \
          -d '{"key": "$VAULT_KEY2"}' \
          http://192.168.56.21:8200/v1/sys/unseal
      '''
      }
    
    withCredentials([string(credentialsId: 'VAULT_KEY3', variable: 'VAULT_KEY3')]) {
      sh '''
      curl \
          -X PUT \
          -d '{"key": "$VAULT_KEY3"}' \
          http://192.168.56.21:8200/v1/sys/unseal
      '''
    }
  }
}
