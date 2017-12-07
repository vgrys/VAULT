#!/usr/bin/groovy
import groovy.json.JsonSlurper

def call(URL) {
//    try {
        echo "********* Upload templates to the NiFi ************"
        uploadTemplate(URL, env)
//    } catch (err) {
//        currentBuild.result = "FAILURE"
//        echo "********* Errors happened *********"
//        throw err
//    }
}

def uploadTemplate(URL, env) {
    List templateId = []
    File[] files = findTemplates(env)
    for (File file : files) {
        echo "start for loop"
        sh "curl -F template=@${file} -X POST  ${URL}/nifi-api/process-groups/root/templates/upload > result"
        echo "********** IN DSS ********************"
        def output = readFile('result').trim()
        echo output
        def result = new XmlSlurper().parseText("${output}")
        echo "Name of the template is: '${result.template.name}'"
        echo "ID of the template is: '${result.template.id}'"
        templateId << "${result.template.id}"
        println(templateId)
    }
    newList = templateId.join(",")
    println(newList)
    echo "End of uploadTemplate"
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


def createWorkspace() {

}
