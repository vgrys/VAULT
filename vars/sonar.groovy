#!/usr/bin/groovy

def scanner(projectName, sonarUrl, sonarTokenId, buildId) {
    def scannerHome = tool name: 'SonarQube3.0.3'
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