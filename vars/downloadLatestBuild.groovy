#!/usr/bin/groovy

import groovy.json.JsonSlurper

def call(artifactoryId, URL, repository, release) {
    withCredentials([usernamePassword(credentialsId: "${artifactoryId}", usernameVariable: 'artifactory_user', passwordVariable: 'artifactory_pwd')]) {
        GString command = "\"content-type: text/plain\" -d 'items.find({ \"repo\":{\"\$eq\":\"${repository}\"}, \"path\":\"atf/${release}\", \"name\":{\"\$match\":\"atf-*.tar.gz\"}})' > JSON"
        sh "curl -u ${artifactory_user}:${artifactory_pwd} -X POST  ${URL}/artifactory/api/search/aql -H ${command}"
        sh "cat JSON"
        def JSON = readFile('JSON').trim()
        def output = new JsonSlurper().parseText(JSON)
        def sortedJSON = output.sort { a,b -> b.updated <=> a.updated}
        print(sortedJSON)

//        for (def result in output.results) {
//            echo "${result.updated} and name: ${result.name}"
////            if (result.updated(result.updated))echo "${result.updated} and name: ${result.name}"
////                return "date1 is after date2";
//            if (result.updated > ) {
//                echo "${result.updated} and name: ${result.name}"
//            }
//
//        }

//        def fileToDownload = "${URL}/artifactory/bigdata-dss-automation/${latestFile}"
    }
}