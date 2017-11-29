#!/usr/bin/groovy

def get (URL, process, id) {
    sh "curl -X GET ${URL}/nifi-api/${process}/${id} > result"
    def output = readFile('result').trim()
    echo "output=$output"
    echo "********** IN DSS ********************"
//    echo "result is: ${result}"

    return result
}

//curl -X GET 192.168.56.105:8088/nifi-api/flow/process-groups/e96237ab-015f-1000-d7ee-a0ea33f79a1e
//
//def post (URL, process, rootID, clientId, name) {
//sh "curl -H \"Content-Type: application/json\" -X POST -d '{\"revision\":{\"clientId\":\"${clientId}\",\"version\":0},\"component\":{\"name\":\"${name}\"}}' ${URL}/nifi-api/${process}/${rootID}/${process}"
////    sh 'curl -H "Content-Type: application/json" -X POST -d '{"revision"':'{"clientId"':'${clientId}',"version":0},"component":{"name":'${name}'}}' "${URL}"/nifi-api/"${process}"/"${rootID}"/"${process}"'
//}
//
//deg getinfo () {
//    sh "http://192.168.56.105:8088/nifi-api/process-groups/ee059263-015f-1000-6d52-da1a4daa9bab"
//}