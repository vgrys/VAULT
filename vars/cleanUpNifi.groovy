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
        def result = get("-X GET ${URL}/nifi-api/flow/process-groups/${processGroup}")
//        List connectionsIds = result.processGroupFlow.flow.connections.id
//        result = null
        for (List id : result.processGroupFlow.flow.connections){
            def status = get("-X POST ${URL}/nifi-api/flowfile-queues/${id}/drop-requests")
            echo "State of clean up queue: '${status.dropRequest.state}'"
        }
    }
}

def deleteProcessGroups(URL) {
    List processGroups = env.PROCESS_GROUPS_ID.split(',')
    processGroups.each{ deleteProcessGroup(URL, it) }
}

def deleteWorkspaceProcessGroup(URL) {
    deleteProcessGroup(URL, env.WORKSPACE_PROCESS_GROUP)
}

def deleteProcessGroup(URL, processGroup) {
    def result = get("-X GET ${URL}/nifi-api/process-groups/${processGroup}")
    String revisionNumber = result.revision.version
    result = null
    sh "curl -X DELETE ${URL}/nifi-api/process-groups/${processGroup}?version=${revisionNumber} > /dev/null 2>&1"
}

def get(url) {
    sh "curl ${url} > output"
    def output = readFile('output').trim()
    return new JsonSlurper().parseText("${output}")
}