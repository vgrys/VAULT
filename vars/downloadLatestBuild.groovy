#!/usr/bin/groovy

import groovy.json.JsonSlurper

def call(artifactoryId, URL, repository, release) {
    def result = getResult(artifactoryId, URL, repository, release)
//    def max = result.results.updated.max()
    def latestBuild = null
    String lastestUpdate = ''
    for (def build : result.results) {
        if (build.updated > lastestUpdate) {
//        if (build.updated == max) {
            latestBuild = build
            lastestUpdate = build.updated
        }
    }
    return latestBuild.name
}

def getResult(artifactoryId, URL, repository, release) {
    withCredentials([usernamePassword(credentialsId: "${artifactoryId}", usernameVariable: 'artifactory_user', passwordVariable: 'artifactory_pwd')]) {
        GString command = "\"content-type: text/plain\" -d 'items.find({ \"repo\":{\"\$eq\":\"${repository}\"}, \"path\":\"atf/${release}\", \"name\":{\"\$match\":\"atf-*.tar.gz\"}})' > output"
        sh "curl -u ${artifactory_user}:${artifactory_pwd} -X POST  ${URL}/artifactory/api/search/aql -H ${command}"
        def output = readFile('output').trim()
        return new JsonSlurper().parseText(output)
    }
}