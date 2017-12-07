#!/usr/bin/groovy
import groovy.json.JsonSlurper
import groovy.util.XmlSlurper
import groovy.io.FileType


def call(URL) {
    try {
        echo "********* Upload templates to the NiFi ************"
        uploadTemplate(URL, env)
    } catch (err) {
        currentBuild.result = "FAILURE"
        echo "********* Errors happened *********"
        throw err
    }
}

def static uploadTemplate (URL, env) {

    def list = []

    def dir = new File("${env.WORKSPACE}/nifi/")
    dir.eachFileRecurse (FileType.FILES) { file ->
        list << file
    }
    list.each {
        return it.path
    }

//    sh "curl -F template=@${templatePath} -X POST  ${URL}/nifi-api/${process}/root/templates/upload > result"
//    echo "********** IN DSS ********************"
//    def output = readFile('result').trim()
//    echo output
//    def result = new XmlSlurper().parseText("${output}")
//    echo "Name of the template is: '${result.template.name}'"
//    echo "ID of the template is: '${result.template.id}'"
}

def getInfo (URL, process, id) {
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

def getInfoConnection (URL, process, id) {
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


def createWorkspace () {

}
