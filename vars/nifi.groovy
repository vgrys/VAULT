#!/usr/bin/groovy

import groovy.json.JsonSlurper

def deploy(URL, projectName) {
    try {
        echo "********* Upload templates to the NiFi ************"
        List templates = uploadTemplates(URL)
        createWorkspace(URL, projectName)
        createProcesGroupsAndDeployTemplate(URL, templates)
    } catch (err) {
        currentBuild.result = "FAILURE"
        echo "********* Errors happened *********"
        throw err
    }
}

def cleanup(URL) {
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

def uploadTemplates(URL) {
    List templates = []
    List templatesId = []
    List template = findTemplates(env)
    for (List name : template) {
        GString filePath = "${env.WORKSPACE}/nifi/${name}"
        sh "curl -F template=@${filePath} -X POST  ${URL}/nifi-api/process-groups/root/templates/upload > output"
        echo "Template '${name}' is uploaded to NiFi server '${URL}'"
        def output = readFile('output').trim()
        print(output)
        def result = new XmlSlurper().parseText(output)
        echo "Template is uploaded with id: '${result.template.id}' and name: '${result.template.name}'"
        String templateId = result.template.id
        String templateName = result.template.name
        templates.add([templateId, templateName])
        templatesId.add(templateId)
    }
    env.TEMPLATE_ID = templatesId.join(',')
    return templates
}

def createWorkspace(URL, projectName) {
    sh "curl -H \"Content-Type: application/json\" -X POST -d '{\"revision\":{\"version\":0},\"component\":{\"name\":\"${projectName}-WORKSPACE\"}}' ${URL}/nifi-api/process-groups/root/process-groups > output"
    def output = readFile('output').trim()
    def result = new JsonSlurper().parseText(output)
    echo "Workspace process group is created with ID: '${result.id}' and name: '${result.component.name}'"
    env.WORKSPACE_PROCESS_GROUP = result.id
}

def createProcesGroupsAndDeployTemplate(URL, templates) {
    List processGroups = []
    for (def template : templates) {
        def templateName = template[1]
        def templateId = template[0]
        echo "Template id is: ${templateId} and name is: ${templateName}"

        def processGroupId = createProcessGroups(URL, templateName)
        processGroups.add(processGroupId)

        def result = deployTemplate(URL, processGroupId, templateId)
        echo result

    }
    env.PROCESS_GROUPS_ID = processGroups.join(',')
    startProcessGroups(URL)
}

def createProcessGroups(URL, templateName){
    GString CreateProcessGroup = "'{\"revision\":{\"version\":0},\"component\":{\"name\":\"${templateName}\"}}' ${URL}/nifi-api/process-groups/${env.WORKSPACE_PROCESS_GROUP}/process-groups"
    sh "curl -H \"Content-Type: application/json\" -X POST -d ${CreateProcessGroup} > output"
    def output = readFile('output').trim()
    def result = new JsonSlurper().parseText(output)
    echo "Process group is created with ID: '${result.id}' and name: '${result.component.name}'"
    String processGroupId = result.id
    return processGroupId
}

def deployTemplate(URL, processGroupId, templateId) {
    GString deployTemplate = "'{\"templateId\":\"${templateId}\",\"originX\":-0.0,\"originY\":-0.0}' ${URL}/nifi-api/process-groups/${processGroupId}/template-instance"
    sh "curl -H \"Content-Type: application/json\" -X POST -d ${deployTemplate} > /dev/null 2>&1"
    return "done"
}

def startProcessGroups(URL) {
    sh "curl -H \"Content-Type: application/json\" -X PUT -d '{\"id\":\"${env.WORKSPACE_PROCESS_GROUP}\",\"state\":\"RUNNING\"}' ${URL}/nifi-api/flow/process-groups/${env.WORKSPACE_PROCESS_GROUP}"
}

def findTemplates(env) {
    sh "ls -A -m -w 5000 ${env.WORKSPACE}/nifi > output"
    def output = readFile('output').trim().toString().split(", ")
    List list = output
    return list
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

def getConnctionsId(URL) {
    List processGroups = env.PROCESS_GROUPS_ID.split(',')
    List connectionsId = []
    for (List processGroup in processGroups) {
        def result = get("-X GET ${URL}/nifi-api/flow/process-groups/${processGroup}")
        def connections = result.processGroupFlow.flow.connections
        result = null
        for (connection in connections) {
            connectionsId.add(connection.id)
        }
    }
    return connectionsId
}

def cleanUpQueue(URL) {
    def result = getConnctionsId(URL)
    for (List id : result){
        def status = get("-X POST ${URL}/nifi-api/flowfile-queues/${id}/drop-requests")
        echo "State of clean up queue: '${status.dropRequest.state}'"
    }
}

def deleteProcessGroups(URL) {
    List processGroups = env.PROCESS_GROUPS_ID.split(',')
    for (processGroup in processGroups) {
        def result = get("-X GET ${URL}/nifi-api/process-groups/${processGroup}")
        String revisionNumber = result.revision.version
        result = null
        sh "curl -X DELETE ${URL}/nifi-api/process-groups/${processGroup}?version=${revisionNumber} > /dev/null 2>&1"
    }
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