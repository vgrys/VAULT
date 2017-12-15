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

@NonCPS
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
        echo "I am here"
//        GString deployTemplate = "'{\"templateId\":\"d29050f0-5627-42f8-b299-e5d8f6a692e7\",\"originX\":-0.0,\"originY\":-0.0}' http://192.168.56.105:8088/nifi-api/process-groups/5ad947e5-0160-1000-e84b-f857cbdd8eef/template-instance"
//        echo deployTemplate
        String command = 'curl -H "Content-Type: application/json" -X POST -d \'{"templateId":"d29050f0-5627-42f8-b299-e5d8f6a692e7","originX":-0.0,"originY":-0.0}\' http://192.168.56.105:8088/nifi-api/process-groups/5ad947e5-0160-1000-e84b-f857cbdd8eef/template-instance > /dev/null 2>&1'
        sh command
//        output = readFile('output').trim()
//        print(output)
        echo "Finished"
//        template["process_group_id"] = id
//        echo template
    }
    env.PROCESS_GROUP_ID = processGroups
}

def deployTemplates() {
    def listId = env.TEMPLATE_ID.toString().split()
    for (String item : listId) {
    }
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

def findTemplates(env) {
    sh "ls -A -m -w 0 ${env.WORKSPACE}/nifi > output"
    def output = readFile('output').trim().toString().split(", ")
    print(output)
    List list = output
    return list
}
