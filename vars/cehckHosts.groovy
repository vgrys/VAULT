#!/usr/bin/groovy

def file = new File('ansible/vars/hosts')
// for example read line by line
def data= file.filterLine { line ->
    line.contains('"ticket":')
}

testRunner.testCase.testSuite.project.setPropertyValue('ticket',data)
