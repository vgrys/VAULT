#!/usr/bin/groovy
import groovy.json.JsonSlurper

def call(URL) {
    def templates
    try {
        echo "********* Upload templates to the NiFi ************"
        templates = uploadTemplates(URL)
        createWorkspace(URL)
        createProcesGroups(URL, templates)
    } catch (err) {
        currentBuild.result = "FAILURE"
        echo "********* Errors happened *********"
        throw err
    }
}

def uploadTemplates(URL) {
    List templatesId = []
    List template = findTemplates(env)
    for (List name : template) {
        GString filePath = "${env.WORKSPACE}/nifi/${name}"
        sh "curl -F template=@${filePath} -X POST  ${URL}/nifi-api/process-groups/root/templates/upload > output"
        echo "Template '${name}' is uploaded to NiFi server ${URL}"
        def output = readFile('output').trim()
        print(output)
        def result = new XmlSlurper().parseText("${output}")
        echo "Template is uploaded with id: '${result.template.id}' and name: '${result.template.name}'"
        String id = result.template.id
        templatesId.add(id)
    }
    env.TEMPLATE_ID = templatesId
    return [{}]
}



def createWorkspace(URL) {
    sh "curl -H \"Content-Type: application/json\" -X POST -d ' {\"revision\":{\"version\":0},\"component\":{\"name\":\"${env.GIT_REPO}-WORKSPACE\"}}' ${URL}/nifi-api/process-groups/root/process-groups > JSON"
    def output = readFile('JSON').trim()
    def result = new JsonSlurper().parseText("${output}")
    echo "Process group is created with ID: '${result.id}' and name: '${result.component.name}'"
    env.WORKSPACE_PROCESS_GROUP = result.id
}

def createProcesGroups(URL) {
    List processGroups = []
    List list = findTemplates(env)
    for (List name : list) {
        def processGroup = name.replace(".xml", "")
        sh "curl -H \"Content-Type: application/json\" -X POST -d ' {\"revision\":{\"version\":0},\"component\":{\"name\":\"${processGroup}\"}}' ${URL}/nifi-api/process-groups/${env.WORKSPACE_PROCESS_GROUP}/process-groups > JSON"
        def output = readFile('JSON').trim()
        def result = new JsonSlurper().parseText("${output}")
        echo "Process group is created with ID: '${result.id}' and name: '${result.component.name}'"
        String id = result.id
        processGroups.add(id)
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
