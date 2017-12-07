#!/usr/bin/groovy
import groovy.io.FileType
import groovy.json.JsonSlurper

def call(URL) {
    try {
        echo "********* Upload templates to the NiFi ************"
        def id = uploadTemplate(URL, env)
        return id
    } catch (err) {
        currentBuild.result = "FAILURE"
        echo "********* Errors happened *********"
        throw err
    }
}

def uploadTemplate(URL, env) {
    List id = []
    File [] files = findTemplates(env)
    for (File file:files) {
        sh "curl -F template=@${file} -X POST  ${URL}/nifi-api/process-groups/root/templates/upload > result"
        echo "********** IN DSS ********************"
        def output = readFile('result').trim()
        result = new XmlSlurper().parseText("${output}")
        echo "Name of the template is: '${result.template.name}'"
        echo "ID of the template is: '${result.template.id}'"
//        env.id = id.add(result.template.id)
    }
}

def static findTemplates(env) {
    File f = new File("${env.WORKSPACE}/nifi")
    File[] matchingFiles = f.listFiles()
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
