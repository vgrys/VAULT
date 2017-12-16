#!/usr/bin/groovy

import groovy.json.JsonSlurper

def call(URL) {
    def templates
    try {
        echo "********* Upload templates to the NiFi ************"

        templateMap = uploadTemplates(URL)
        createWorkspace(URL)
        createProcesGroupsAndDeployTemplate(URL, templateMap)
    } catch (err) {
        currentBuild.result = "FAILURE"
        echo "********* Errors happened *********"
        throw err
    }
}

def uploadTemplates(URL) {
    List templatesId = []
    LinkedHashMap templateMap = [:]
    List template = findTemplates(env)
    for (List name : template) {
        GString filePath = "${env.WORKSPACE}/nifi/${name}"
        sh "curl -F template=@${filePath} -X POST  ${URL}/nifi-api/process-groups/root/templates/upload > output"
        echo "Template '${name}' is uploaded to NiFi server ${URL}"
        def output = readFile('output').trim()
        print(output)
        def result = new XmlSlurper().parseText("${output}")
        echo "Template is uploaded with id: '${result.template.id}' and name: '${result.template.name}'"
        String templateId = result.template.id
        String templateName = result.template.name
        templatesId.add(templateId)
        templateMap."${templateName}" = "${templateId}"
        print(templateMap)
    }
    env.TEMPLATE_ID = templatesId
    return templateMap
}

def createWorkspace(URL) {
    sh "curl -H \"Content-Type: application/json\" -X POST -d '{\"revision\":{\"version\":0},\"component\":{\"name\":\"${env.GIT_REPO}-WORKSPACE\"}}' ${URL}/nifi-api/process-groups/root/process-groups > JSON"
    def output = readFile('JSON').trim()
    def result = new JsonSlurper().parseText("${output}")
    echo "Process group is created with ID: '${result.id}' and name: '${result.component.name}'"
    env.WORKSPACE_PROCESS_GROUP = result.id
}

def createProcesGroupsAndDeployTemplate(URL, templateMap) {

    List processGroups = []
    for (def key : templateMap.keySet()) {

        String templateName = key
        String templateId = templateMap.getAt(key)
        echo "templateId is: ${templateId} and name is: ${templateName}"
        GString CreateProcessGroup = "'{\"revision\":{\"version\":0},\"component\":{\"name\":\"${templateName}\"}}' ${URL}/nifi-api/process-groups/${env.WORKSPACE_PROCESS_GROUP}/process-groups"
        sh "curl -H \"Content-Type: application/json\" -X POST -d ${CreateProcessGroup} > output"
        def output = readFile('output').trim()
        def result = new JsonSlurper().parseText("${output}")
        echo "Process group is created with ID: '${result.id}' and name: '${result.component.name}'"
        String processGroupId = result.id
        processGroups.add(processGroupId)
        result = ''
        GString deployTemplate = "'{\"templateId\":\"${templateId}\",\"originX\":-0.0,\"originY\":-0.0}' ${URL}/nifi-api/process-groups/${processGroupId}/template-instance"
        sh "curl -H \"Content-Type: application/json\" -X POST -d ${deployTemplate} > /dev/null 2>&1"
        sh "curl -H \"Content-Type: application/json\" -X PUT -d '{\"id\":\"${processGroupId}\",\"state\":\"RUNNING\"}' ${URL}/nifi-api/flow/process-groups/${processGroupId}"
//        output = readFile('output').trim()
//        print(output)
//        template["process_group_id"] = id
//        echo template
    }
    env.PROCESS_GROUP_ID = processGroups
}

def findTemplates(env) {
    sh "ls -A -m -w 0 ${env.WORKSPACE}/nifi > output"
    def output = readFile('output').trim().toString().split(", ")
    print(output)
    List list = output
    return list
}
