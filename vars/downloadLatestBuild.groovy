#!/usr/bin/groovy

import groovy.json.JsonSlurper


def call(artifactoryId, URL, repository, release) {
    withCredentials([usernamePassword(credentialsId: "${artifactoryId}", usernameVariable: 'artifactory_user', passwordVariable: 'artifactory_pwd')]) {
        GString command = "\"content-type: text/plain\" -d 'items.find({ \"repo\":{\"\$eq\":\"${repository}\"}, \"path\":\"atf/${release}\", \"name\":{\"\$match\":\"atf-*.tar.gz\"}})' > output"
        sh "curl -u ${artifactory_user}:${artifactory_pwd} -X POST  ${URL}/artifactory/api/search/aql -H ${command}"
        sh "cat output"
        def output = readFile('output').trim()
        def result = new JsonSlurper().parseText(output)
        def max = result.results.updated.max()
        latestBuild = result.results.find { it.updated == max }
        print(latestBuild.name)
        return latestBuild.name
    }

}