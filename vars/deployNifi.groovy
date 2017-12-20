#!/usr/bin/groovy

import groovy.json.JsonSlurper
import java.util.Map

def call(URL, projectName) {
    try {
        echo "********* Upload templates to the NiFi ************"
        Map<String, String> templateMap = uploadTemplates(URL)

//        templateMap = uploadTemplates(URL)
        createWorkspace(URL, projectName)
        createProcesGroupsAndDeployTemplate(URL, templateMap)
    } catch (err) {
        currentBuild.result = "FAILURE"
        echo "********* Errors happened *********"
        throw err
    }
}

def uploadTemplates(URL) {
    List templatesId = []
//    Map<String, String> templateMap = [:]
    Map<String, String> templateMap = new HashMap()
    List template = findTemplates(env)
    for (List name : template) {
        GString filePath = "${env.WORKSPACE}/nifi/${name}"
        sh "curl -F template=@${filePath} -X POST  ${URL}/nifi-api/process-groups/root/templates/upload > output"
        echo "Template '${name}' is uploaded to NiFi server '${URL}'"
        def output = readFile('output').trim()
        print(output)
        def result = new XmlSlurper().parseText("${output}")
        echo "Template is uploaded with id: '${result.template.id}' and name: '${result.template.name}'"
        String templateId = result.template.id
        String templateName = result.template.name
        templatesId.add(templateId)
        templateMap."${templateName}" = "${templateId}"
    }
    env.TEMPLATE_ID = templatesId.join(',')
    return templateMap
}

def createWorkspace(URL, projectName) {
    sh "curl -H \"Content-Type: application/json\" -X POST -d '{\"revision\":{\"version\":0},\"component\":{\"name\":\"${projectName}-WORKSPACE\"}}' ${URL}/nifi-api/process-groups/root/process-groups > output"
    def output = readFile('output').trim()
    def result = new JsonSlurper().parseText("${output}")
    echo "Workspace process group is created with ID: '${result.id}' and name: '${result.component.name}'"
    env.WORKSPACE_PROCESS_GROUP = result.id
}

@NonCPS
def createProcesGroupsAndDeployTemplate(URL, templateMap) {
    List processGroups = []
    for (def key : templateMap.keySet()) {
        String templateName = key
        String templateId = templateMap.get(key)
        echo "Template id is: ${templateId} and name is: ${templateName}"

        def processGroupId = createProcessGroups(URL, templateName)
        processGroups.add(processGroupId)

        def result = deployTemplate(URL, processGroupId, templateId)
        echo result

//        GString CreateProcessGroup = "'{\"revision\":{\"version\":0},\"component\":{\"name\":\"${templateName}\"}}' ${URL}/nifi-api/process-groups/${env.WORKSPACE_PROCESS_GROUP}/process-groups"
//        sh "curl -H \"Content-Type: application/json\" -X POST -d ${CreateProcessGroup} > output"
//        def output = readFile('output').trim()
//        def result = new JsonSlurper().parseText("${output}")
//        echo "Process group is created with ID: '${result.id}' and name: '${result.component.name}'"
//        String processGroupId = result.id
//        processGroups.add(processGroupId)
//        result = null

//        GString deployTemplate = "'{\"templateId\":\"${templateId}\",\"originX\":-0.0,\"originY\":-0.0}' ${URL}/nifi-api/process-groups/${processGroupId}/template-instance"
//        sh "curl -H \"Content-Type: application/json\" -X POST -d ${deployTemplate} > /dev/null 2>&1"
//        sh "curl -H \"Content-Type: application/json\" -X PUT -d '{\"id\":\"${processGroupId}\",\"state\":\"RUNNING\"}' ${URL}/nifi-api/flow/process-groups/${processGroupId}"
    }
    env.PROCESS_GROUPS_ID = processGroups.join(',')
    startProcessGroups(URL)
}

def createProcessGroups(URL, templateName){
    GString CreateProcessGroup = "'{\"revision\":{\"version\":0},\"component\":{\"name\":\"${templateName}\"}}' ${URL}/nifi-api/process-groups/${env.WORKSPACE_PROCESS_GROUP}/process-groups"
    sh "curl -H \"Content-Type: application/json\" -X POST -d ${CreateProcessGroup} > output"
    def output = readFile('output').trim()
    def result = new JsonSlurper().parseText("${output}")
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
