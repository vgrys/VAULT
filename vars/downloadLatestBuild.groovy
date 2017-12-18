#!/usr/bin/groovy

import groovy.json.JsonSlurper

def call(artifactoryId, URL, repository, release) {
    withCredentials([usernamePassword(credentialsId: "${artifactoryId}", usernameVariable: 'artifactory_user', passwordVariable: 'artifactory_pwd')]) {
        GString command = "\"content-type: text/plain\" -d 'items.find({ \"repo\":{\"\$eq\":\"${repository}\"}, \"path\":\"atf/${release}\", \"name\":{\"\$match\":\"atf-*.tar.gz\"}})' > output"
        sh "curl -u ${artifactory_user}:${artifactory_pwd} -X POST  ${URL}/artifactory/api/search/aql -H ${command}"
        sh "cat output"
        def outputJson = readFile('output').trim()
        def output = new JsonSlurper().parseText(outputJson)
        print(output.results.name)
        def max = output.results.updated.max()
        def result = output.results.updated.groupBy {it}.get(max)
        print(result)
        if (max) {
            print(output.results.updated.groupBy {it}.get(output.results.name))
        }
    }
}