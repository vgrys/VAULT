#!/usr/bin/groovy

import groovy.json.JsonSlurper

def call(URL) {
    try {
        echo "********* Delete templates from NiFi ************"
        deleteTemplates(URL)
        stopProcessGroup(URL)
        cleanUpQueue(URL)
        deleteProcessGroups(URL)
        deleteWorkspaceProcessGroup(URL)
    } catch (err) {
        currentBuild.result = "FAILURE"
        echo "********* Errors happened *********"
        throw err
    }
}

def deleteTemplates(URL) {
    List templates = env.TEMPLATE_ID.split(',')
    for (List template : templates) {
        sh "curl -X DELETE ${URL}/nifi-api/templates/${template}"
    }
    echo "Templates are removed"
}

def stopProcessGroup(URL) {
    sh "curl -H \"Content-Type: application/json\" -X PUT -d '{\"id\":\"${env.WORKSPACE_PROCESS_GROUP}\",\"state\":\"STOPPED\"}' ${URL}/nifi-api/flow/process-groups/${env.WORKSPACE_PROCESS_GROUP}"
}

def cleanUpQueue(URL) {
    List processGroups = env.PROCESS_GROUPS_ID.split(',')
    print(processGroups)
    for (List processGroup in processGroups) {
//        sh "curl -X GET ${URL}/nifi-api/flow/process-groups/${processGroup} > output"
//        def output = readFile('output').trim()
//        def result = new JsonSlurper().parseText("${output}")
        def result = get("GET ${URL}/nifi-api/flow/process-groups/${processGroup}")
        echo "connections ID is: '${result.processGroupFlow.flow.connections.id}'"
        List connectionsIds = result.processGroupFlow.flow.connections.id
        result = null
        for (List connectionsId : connectionsIds){
//            sh "curl -X POST ${URL}/nifi-api/flowfile-queues/${connectionsId}/drop-requests > output"
//            def jsonStatus = readFile('output').trim()
//            def status = new JsonSlurper().parseText("${jsonStatus}")
            def status = get("POST ${URL}/nifi-api/flowfile-queues/${connectionsId}/drop-requests")
            echo "State of clean up queue: '${status.dropRequest.state}'"
        }
    }
}

def deleteProcessGroups(URL) {
    List processGroups = env.PROCESS_GROUPS_ID.split(',')
    processGroups.each{ deleteProcessGroup(URL, it) }
//    for (List processGroup in processGroups) {
//        deleteProcessGroup(URL, processGroup)
//    }
}

def deleteWorkspaceProcessGroup(URL) {
    deleteProcessGroup(URL, env.WORKSPACE_PROCESS_GROUP)
}

def deleteProcessGroup(URL, processGroup) {
//    sh "curl -X GET ${URL}/nifi-api/process-groups/${processGroup} > output"
//    def output = readFile('output').trim()
//    def result = new JsonSlurper().parseText("${output}")
    def result = get("GET ${URL}/nifi-api/process-groups/${processGroup}")
    String revisionNumber = result.revision.version
    result = null
    sh "curl -X DELETE ${URL}/nifi-api/process-groups/${processGroup}?version=${revisionNumber} > /dev/null 2>&1"
}

def get(url) {
    sh "curl -X ${url} > output"
    def output = readFile('output').trim()
    return new JsonSlurper().parseText("${output}")
}
//def deserializeList(strList) {
//    strList.split(',')
//}
//
//def searializeList(list) {
//    list.join(',')
//}