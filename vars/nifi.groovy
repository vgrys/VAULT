#!/usr/bin/groovy
import groovy.json.JsonSlurper

def call(URL) {
    try {
        echo "********* Upload templates to the NiFi ************"
        uploadTemplate(URL)
        createWorkspace(URL)
        getTemplatesId(URL)
//        createProcesGroups(URL)
    } catch (err) {
        currentBuild.result = "FAILURE"
        echo "********* Errors happened *********"
        throw err
    }
}

def uploadTemplate(URL) {
    List list = findTemplates(env)
    File fileResult = new File("${env.WORKSPACE}/templatesResult")
    for (List file : list) {
        GString filePath = "${env.WORKSPACE}/nifi/${file}"
        sh "curl -F template=@${filePath} -X POST  ${URL}/nifi-api/process-groups/root/templates/upload > XML"
        def output = readFile('XML').trim()
        print(output)
        def result = new XmlSlurper().parseText("${output}")
        echo "Name of the template is: '${result.template.name}'"
        echo "ID of the template is: '${result.template.id}'"
        env.TEMPLATE_ID = result.template.id.append()
        print(env.TEMPLATE_ID)
    }
//    env.TEMPLATE_ID = readFile("${fileResult}").trim()
}


def createWorkspace(URL) {
    sh "curl -H \"Content-Type: application/json\" -X POST -d ' {\"revision\":{\"version\":0},\"component\":{\"name\":\"${env.GIT_REPO}-WORKSPACE\"}}' ${URL}/nifi-api/process-groups/root/process-groups > JSON"
    def output = readFile('JSON').trim()
    def result = new JsonSlurper().parseText("${output}")
    echo "Process group is created with ID: '${result.id}' and name: '${result.component.name}'"
    env.WORKSPACE_PROCESS_GROUP = result.id
}

def getTemplatesId(URL) {
    List list = findTemplates(env)
    List templatesId = []
    List templatesName = []
    sh "curl -X GET ${URL}/nifi-api/flow/templates > output"
    def output = readFile('output').trim()
    def result = new JsonSlurper().parseText("${output}")
    print(result)
    print(result.templates.template.name)
    print(result.templates.template.id)
    for (List templateName : list) {
        echo templateName
        def templates = templateName.replace(".xml", "")
        print("templates is: ${templates}")
        echo "I am in for loop"
        if (result.templates.template.name == templates) {
            echo "I am in if loop"
            echo "templateName is: ${templateName}"
            echo "result.templates.template.name is: ${result.templates.template.name}"
            templatesId < result.templates.template.id
            templatesName < result.templates.template.name
            echo result.templates.template.id
            echo result.templates.template.name
        } else {
            echo "I am in else loop"
        }
        env.TEMPLATE_ID = templatesId
    }
    echo "Process group is created with ID: '${result.templates.template}'"
}

def createProcesGroups(URL) {
    List list = findTemplates(env)
//    File fileResult = new File("${env.WORKSPACE}/groupsResult")
    for (List file : list) {
        println(file.getName().replace(".xml", ""))
        def processGroup = file.getName().replace(".xml", "")
        sh "curl -H \"Content-Type: application/json\" -X POST -d ' {\"revision\":{\"version\":0},\"component\":{\"name\":\"${processGroup}\"}}' ${URL}/nifi-api/process-groups/${env.WORKSPACE_PROCESS_GROUP}/process-groups > JSON"
        def output = readFile('JSON').trim()
        def result = new JsonSlurper().parseText("${output}")
        echo "Process group is created with ID: '${result.id}' and name: '${result.component.name}'"
        fileResult << ("${result.id} ")

    }
    env.PROCESS_GROUP_ID = readFile("${fileResult}").trim()
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
