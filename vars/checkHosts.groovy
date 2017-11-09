#!/usr/bin/groovy

def call(targetGroup) {
    def file = new File("${env.WORKSPACE}/ansible/vars/hosts")
        // for example read line by line
    def data = file.filterLine { line ->
        if (line.contains('[prod]')) {
            print "we are good to go"
        } else {
            print "no targetGroup '${targetGroup}' found"
        }
    }
}
//testRunner.testCase.testSuite.project.setPropertyValue('ticket',data)
