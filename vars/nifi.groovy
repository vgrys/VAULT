#!/usr/bin/groovy
import groovy.json.JsonSlurper

def call(URL) {
    try {
        echo "********* Upload templates to the NiFi ************"
        uploadTemplate(URL, env)
        createWorkspace(URL)
    } catch (err) {
        currentBuild.result = "FAILURE"
        echo "********* Errors happened *********"
        throw err
    }
}

def uploadTemplate(URL, env) {
    File[] files = findTemplates(env)
    File fileResult = new File("${env.WORKSPACE}/nifi/templateResult")
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
    env.TEMPLATE_ID = readFile("${fileResult}").trim().replace(" ", ",")
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

def createWorkspace(URL) {
    sh "curl -H \"Content-Type: application/json\" -X POST -d ' {\"revision\":{\"version\":0},\"component\":{\"name\":\"test-to-delete-from-linux\"}}' ${URL}/nifi-api/process-groups/root/process-groups > JSON"
    def output = readFile('XML').trim()
    def result = new JsonSlurper().parseText("${output}")
    echo "Group ID is: '${result.id}'"
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

