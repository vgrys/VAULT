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

def uploadTemplate(URL) {
    File[] files = findTemplates(env)
    File fileResult = new File("${env.WORKSPACE}/temp/templateResult")
    for (File file : files) {
        sh "curl -F template=@${file} -X POST  ${URL}/nifi-api/process-groups/root/templates/upload > XML"
        def output = readFile('XML').trim()
        echo output
        def result = new XmlSlurper().parseText("${output}")
        echo "Name of the template is: '${result.template.name}'"
        result.template.id.each {
            fileResult << ("${it} ")
        }
    }
    env.TEMPLATE_ID = readFile("${fileResult}").trim()
}

def createWorkspace(URL) {
    sh "curl -H \"Content-Type: application/json\" -X POST -d ' {\"revision\":{\"version\":0},\"component\":{\"name\":\"${env.GIT_REPO}\"}}' ${URL}/nifi-api/process-groups/root/process-groups > JSON"
    def output = readFile('JSON').trim()
    def result = new JsonSlurper().parseText("${output}")
    echo "Group ID is: '${result.id}'"
    env.WORKSPACE_PROCESS_GROUP = result.id
}

def createProcesGroups(URL) {
    def listId = env.TEMPLATE_ID.toString().split()
    File[] files = findTemplates(env)
    for (File file : files) {
        println(file.getName().replace(".xml", ""))

//        for (String item : listId) {
//
//            sh "curl -H \"Content-Type: application/json\" -X POST -d ' {\"revision\":{\"version\":0},\"component\":{\"name\":\"${env.GIT_REPO}\"}}' ${URL}/nifi-api/process-groups/${env.WORKSPACE_PROCESS_GROUP}/process-groups > JSON"
//            def output = readFile('JSON').trim()
//            def result = new JsonSlurper().parseText("${output}")
//            echo "Group ID is: '${result.id}'"
//        }
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

def static findTemplates(env) {
    File files = new File("${env.WORKSPACE}/nifi")
    File[] matchingFiles = files.listFiles()
    return matchingFiles
}

//        File f = new File("${env.WORKSPACE}/nifi")
//        File[] matchingFiles = f.listFiles()
//        for (File file:matchingFiles) {
//            println(file.getName().replace(".xml", ""))
//            println(file)
//        }
