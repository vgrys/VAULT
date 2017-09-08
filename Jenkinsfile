#!groovy
node {
  //
  def vault_server = 'http://192.168.56.21:8200'
  // repository on Artifactory server
  withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'VAULT_TOKEN')]) {
    sh '''
    set +x
    echo "My secret is token: $VAULT_TOKEN"
  }
}
