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
  
    withCredentials([string(credentialsId: 'VAULT-KEY1', variable: 'VAULT-KEY1')]) {
      sh '''
      echo "My KEY1 is: $VAULT-KEY1"
      '''
     }
  
    withCredentials([string(credentialsId: 'VAULT-KEY2', variable: 'VAULT-KEY2')]) {
      sh '''
      echo "My KEY2 is: $VAULT-KEY2" 
      '''
      }
    
    withCredentials([string(credentialsId: 'VAULT-KEY3', variable: 'VAULT-KEY3')]) {
      sh '''
      echo "My KEY3 is: $VAULT-KEY3"
      '''
    }
  }
}
