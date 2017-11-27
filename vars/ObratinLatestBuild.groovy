#!/usr/bin/env groovy

// Artifactory settings

//def runDeployATF(ansibleCommand, artifactoryId) {
//    withCredentials([usernamePassword(credentialsId: "${artifactoryID}", usernameVariable: 'artifactory_user', passwordVariable: 'artifactory_pwd')]) {
//        sh ansibleCommand            }
//        }
//    }
//}

def result () {
    GString curl = "curl -u $username:$password -X POST  http://$host/artifactory/api/search/aql -H"
    resultAsJson = ["$curl \"content-type: text/plain\" -d 'items.find({ \"repo\": {\"$eq\":\"bigdata-dss-automation\"}, \"path\" : \"atf/release\", \"name\": {\"$match\" : \"atf-*\"}})'"].execute().text

    echo resultAsJson
}

//def repoUrl= "https://gitblit.myhost.com/git/" + repository + ".git"
//json='{"repository":{"url":"'+repoUrl+'"}}'
//
//def response = "curl -v -k -X POST -H \"Content-Type: application/json\" -d '${json}' https://username:password@anotherhost.com:9443/restendpoint".execute().text
//println response
