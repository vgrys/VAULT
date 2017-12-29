#!/usr/bin/groovy

def scanner(sonarUrl, sonarTokenId, sonarQubeScanner, projectName, buildId) {
    def scannerHome = tool name: "${sonarQubeScanner}"
    withCredentials([string(credentialsId: "${sonarTokenId}", variable: 'sonarToken')]) {
        dir("${env.WORKSPACE}") {
            sh "${scannerHome}/bin/sonar-scanner " +
                    "-Dsonar.projectKey=${projectName} " +
                    "-Dsonar.sources=. " +
                    "-Dsonar.host.url=${sonarUrl}/sonar " +
                    "-Dsonar.login=${sonarToken} " +
                    "-Dsonar.projectName=${projectName} " +
                    "-Dsonar.projectVersion=${buildId} "
        }
    }
}