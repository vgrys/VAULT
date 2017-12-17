#!/usr/bin/groovy

import groovy.json.JsonSlurper

def call(URL) {
    try {
        echo "********* Delete templates from NiFi ************"
        deleteTemplates(URL)
        stopProcessGroup(URL)
        cleanUpQueue(URL)
        getInfo(URL)
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

def cleanUpQueue(URL) {
    List processGroups = env.PROCESS_GROUP_ID.replace("[", "").replace("]", "").split(', ')
    print(processGroups)
    for (List processGroup in processGroups) {
        sh "curl -X GET ${URL}/nifi-api/flow/process-groups/${processGroup} > output"
        def output = readFile('output').trim()
        def result = new JsonSlurper().parseText("${output}")
        echo "connections ID is: '${result.processGroupFlow.flow.connections.id}'"
        List connectionsIds = result.processGroupFlow.flow.connections.id
        result = null
        for (List connectionsId : connectionsIds){
            sh "curl -X POST ${URL}/nifi-api/flowfile-queues/${connectionsId}/drop-requests > output"
            def jsonStatus = readFile('output').trim()
            def status = new JsonSlurper().parseText("${jsonStatus}")
            echo "State of clean up queue: '${status.dropRequest.state}'"
        }
    }
}

def getInfo(URL) {
    sh "curl -X GET ${URL}/nifi-api/process-groups/${env.WORKSPACE_PROCESS_GROUP} > output"
    def output = readFile('output').trim()
    def result = new JsonSlurper().parseText("${output}")
    echo "Group ID is: '${result.component.id}'"
    echo " Group name is: '${result.component.name}'"
    echo "URI is: '${result.uri}'"
    echo "revision version is: '${result.revision.version}'"
    echo "parentGroupId is: '${result.component.parentGroupId}'"
}