#!/usr/bin/groovy

def get (URL, process, id) {
    sh "curl -X GET ${URL}/nifi-api/${process}/${id}"
}

//curl -X GET 192.168.56.105:8088/nifi-api/flow/process-groups/e96237ab-015f-1000-d7ee-a0ea33f79a1e

def post (URL, process, rootID, name) {
sh "curl -H \"Content-Type: application/json\" -X POST -d '{\"revision\":{\"version\":0},\"component\":{\"name\":\"test123\"}}' ${URL}/nifi-api/${process}/${rootID}/process-groups"
//    sh 'curl -H "Content-Type: application/json" -X POST -d '{"revision"':'{"clientId"':'${clientId}',"version":0},"component":{"name":'${name}'}}' "${URL}"/nifi-api/"${process}"/"${rootID}"/"${process}"'
}