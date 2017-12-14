#!/usr/bin/groovy

import groovy.json.JsonSlurper

def call(artifactoryId, URL) {
    withCredentials([usernamePassword(credentialsId: "${artifactoryId}", usernameVariable: 'artifactory_user', passwordVariable: 'artifactory_pwd')]) {
        sh "curl -u ${artifactory_user}:${artifactory_pwd} -X POST  ${URL}/artifactory/api/search/aql -H \"content-type: text/plain\" -d 'items.find({ \"repo\": {\"\$eq\":\"bigdata-dss-automation\"}, \"path\" : \"atf/release\", \"name\": {\"\$match\" : \"atf-*.tar.gz\"}}).sort({\"\$asc\" : [\"results\",\"updated\"]})' > JSON"
        sh "cat JSON"
        def JSON = readFile('JSON').trim()
        def output = new JsonSlurper().parseText(JSON)
        for (def result in output.results) {
            echo result.updated
//        def sortedJSON = result.sort { a,b -> b.updated <=> a.updated}

//                    def id = sortedJSON[0].id
        }

//        def fileToDownload = "${URL}/artifactory/bigdata-dss-automation/${latestFile}"
    }
//        Use ./jq to pars JSON
//latestFile=$(echo ${resultAsJson} |  jq -r '.results | sort_by("updated") [-1].name')

}