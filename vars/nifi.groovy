#!/usr/bin/groovy
import groovy.io.FileType
import groovy.json.JsonSlurper

//def call(URL) {
//    try {
//        echo "********* Upload templates to the NiFi ************"
//        def test = uploadTemplate(URL, env)
//        return test
//    } catch (err) {
//        currentBuild.result = "FAILURE"
//        echo "********* Errors happened *********"
//        throw err
//    }
//}
def uploadTemplate(URL) {

    List list = []

    File dir = new File("${env.WORKSPACE}/nifi")
    dir.eachFileRecurse(FileType.FILES, println(it.name)) { file ->
    println(file)
        list << file
    }
    echo "I am here"
    println(list)


//
//    new File("${env.WORKSPACE}/nifi").eachFile(FileType.FILES, {list << it.name })
//    echo "I am here"
//    println(list)



//    return list

//    new File("${env.WORKSPACE}/nifi").eachFile() { file->
//        list.add (file.getName())
//    }
//            eachFileRecurse { list.add(it.name) }


}

//    list.each {
//    }

//    sh "curl -F template=@${templatePath} -X POST  ${URL}/nifi-api/${process}/root/templates/upload > result"
//    echo "********** IN DSS ********************"
//    def output = readFile('result').trim()
//    echo output
//    def result = new XmlSlurper().parseText("${output}")
//    echo "Name of the template is: '${result.template.name}'"
//    echo "ID of the template is: '${result.template.id}'"


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
