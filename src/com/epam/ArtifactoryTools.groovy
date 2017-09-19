package com.epam


class ArtifactoryTools implements Serializable {
    def steps
    ArtifactoryTools(steps) {this.steps = steps}
    def printName() {
        // evaluate the body block, and collect configuration into the object
        println "Hello world!"
    }
}

//def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
//
//
//def jobBaseName = "${env.JOB_NAME}".split('/')


