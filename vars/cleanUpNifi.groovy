#!/usr/bin/groovy

import groovy.json.JsonSlurper

def call(URL) {
    try {
        echo "********* Upload templates to the NiFi ************"
        deleteTemplates(URL)
        stopProcessGroup(URL)
    } catch (err) {
        currentBuild.result = "FAILURE"
        echo "********* Errors happened *********"
        throw err
    }
}

def deleteTemplates(URL) {
    List templates = env.TEMPLATE_ID.replace("[", "").replace("]", "").split(', ')
    for (List template : templates) {
        sh "curl -X DELETE ${URL}/nifi-api/templates/${template}"
    }
    echo "Templates are removed"
}

def stopProcessGroup(URL) {
    sh "curl -H \"Content-Type: application/json\" -X PUT -d '{\"id\":\"${env.WORKSPACE_PROCESS_GROUP}\",\"state\":\"STOPPED\"}' ${URL}/nifi-api/flow/process-groups/${env.WORKSPACE_PROCESS_GROUP}"
}

def getInfo(URL, process, id) {
    sh "curl -X GET ${URL}/nifi-api/${process}/${id} > result"
    def output = readFile('result').trim()
    echo "********** IN DSS ********************"
    def sluper = new JsonSlurper()
    def result = sluper.parseText("${output}")
    echo "Group ID is: '${result.component.id}'"
    echo " Group name is: '${result.component.name}'"
    echo "URI is: '${result.uri}'"
    echo "revision version is: '${result.revision.version}'"
    echo "parentGroupId is: '${result.component.parentGroupId}'"
}

def getInfoConnection(URL, process, id) {
    sh "curl -X GET ${URL}/nifi-api/flow/${process}/${id} > result"
    def output = readFile('result').trim()
    echo "********** IN DSS ********************"
    def sluper = new JsonSlurper()
    def result = sluper.parseText("${output}")
    echo "Group ID is: '${result.processGroupFlow.id}'"
    echo "Group name is: '${result.processGroupFlow.breadcrumb.breadcrumb.name}'"
    echo "connections ID is: '${result.processGroupFlow.flow.connections.id}'"
    echo "URI is: '${result.processGroupFlow.flow.connections.uri}'"
}