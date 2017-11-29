#!/usr/bin/groovy
import groovy.json.JsonSlurper
import groovy.util.XmlSlurper


def get (URL, process, id) {
    sh "curl -X GET ${URL}/nifi-api/${process}/${id} > result"
    def output = readFile('result').trim()
    echo "********** IN DSS ********************"
    def sluper = new JsonSlurper()
    def result = sluper.parseText("${output}")
//    assert result.component.id
    echo "Group ID is: '${result.component.id}'"
    echo " Group name is: '${result.component.name}'"
    echo "URI is: '${result.uri}'"
    echo "revision version is: '${result.revision.version}'"
    echo "parentGroupId is: '${result.component.parentGroupId}'"
}

def uploadTemplate (nifiURL, process, nifiRootID, nifiClientID, groupFromJenkins, templatePath) {
    sh "curl -F template=@${templatePath} -X POST  http://192.168.56.105:8088/nifi-api/process-groups/root/templates/upload > result"
    echo "********** IN DSS ********************"
    def output = readFile('result').trim()
    def result = new XmlSlurper().parseText("${output}")
    echo "Group ID is: '${result.technology.name.output()}'"

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