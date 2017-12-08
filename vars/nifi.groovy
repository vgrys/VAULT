#!/usr/bin/groovy
import groovy.json.JsonSlurper

def call(URL) {
    try {
        echo "********* Upload templates to the NiFi ************"
        uploadTemplate(URL)
        createWorkspace(URL)
        createProcesGroups(URL)
    } catch (err) {
        currentBuild.result = "FAILURE"
        echo "********* Errors happened *********"
        throw err
    }
}

def static findTemplates(env) {
//    File files = new File("${env.WORKSPACE}/nifi")
//    File[] matchingFiles = files.listFiles()
    def array = "ls -A ${env.WORKSPACE}/nifi".execute().text.trim().toString().split()
//    List list = Arrays.asList(array)
//    print(list)
//    for (String item : array) {
//        print(item)
//    }
    return array
}

def uploadTemplate(URL) {
    files = findTemplates(env)
    File fileResult = new File("${env.WORKSPACE}/templatesResult")
    for (File name : files) {
        GString file = "${env.WORKSPACE}/nifi/${name}"
        sh "curl -F template=@${file} -X POST  ${URL}/nifi-api/process-groups/root/templates/upload > XML"
        def output = readFile('XML').trim()
        echo output
        def result = new XmlSlurper().parseText("${output}")
        echo "Name of the template is: '${result.template.name}'"
        fileResult << ("${result.template.id} ")
    }
    env.TEMPLATE_ID = readFile("${fileResult}").trim()
}

def createWorkspace(URL) {
    sh "curl -H \"Content-Type: application/json\" -X POST -d ' {\"revision\":{\"version\":0},\"component\":{\"name\":\"${env.GIT_REPO}-WORKSPACE\"}}' ${URL}/nifi-api/process-groups/root/process-groups > JSON"
    def output = readFile('JSON').trim()
    def result = new JsonSlurper().parseText("${output}")
    echo "Process group is created with ID: '${result.id}' and name: '${result.component.name}'"
    env.WORKSPACE_PROCESS_GROUP = result.id
}

def createProcesGroups(URL) {
    files = findTemplates(env)
    File fileResult = new File("${env.WORKSPACE}/groupsResult")
    for (File file : files) {
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



