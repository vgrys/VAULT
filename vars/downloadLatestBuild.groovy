#!/usr/bin/groovy

import groovy.json.JsonSlurper

def call(artifactoryId, URL) {
    withCredentials([usernamePassword(credentialsId: "${artifactoryId}", usernameVariable: 'artifactory_user', passwordVariable: 'artifactory_pwd')]) {
        sh "curl -u ${artifactory_user}:${artifactory_pwd} -X POST  ${URL}/artifactory/api/search/aql -H \"content-type: text/plain\" -d 'items.find({ \"repo\": {\"\$eq\":\"bigdata-dss-automation\"}, \"path\" : \"atf/release\", \"name\": {\"\$match\" : \"atf-*.tar.gz\"}})' > output"
        sh "cat output"
        def result = new JsonSlurper().parseText(output)
        def sortedJSON = result.sort { a,b -> b.results.updated <=> a.results.updated}

        def id = sortedJSON[0].id
        echo id

        def fileToDownload = "${URL}/artifactory/bigdata-dss-automation/${latestFile}"
    }
//        Use ./jq to pars JSON
//latestFile=$(echo ${resultAsJson} |  jq -r '.results | sort_by("updated") [-1].name')

}