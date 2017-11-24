#!/usr/bin/groovy

def get (URL, process, id) {
    sh "curl -X GET ${URL}/nifi-api/${process}/${id}"
}

//curl -X GET 192.168.56.105:8088/nifi-api/flow/process-groups/e96237ab-015f-1000-d7ee-a0ea33f79a1e