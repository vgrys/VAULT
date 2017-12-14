#!/bin/bash

# Artifactory settings
host="192.168.56.105:8081"
username="grys"
password="AKCp5Z2Y3srMsSpGGpfEeGWu7FTQdexXPAfhxF5stW6mYh1TfrkwPmopZ5Yq4MgGH3BfgskkE"

resultAsJson=(curl -u $username:$password -X POST  http://$host/artifactory/api/search/aql -H "content-type: text/plain" -d 'items.find({ "repo": {"$eq":"bigdata-dss-automation"}, "path" : "atf/release", "name": {"$match" : "atf-*.tar.gz"}})')

# Use ./jq to pars JSON
latestFile=$(echo ${resultAsJson} |  jq -r '.results | sort_by("updated") [-1].name')

FileToDownload="http://$host/artifactory/bigdata-dss-automation/$latestFile"
curl -u $username:$password -O $FileToDownload

echo ''
echo "Downloaded: $FileToDownload"
echo ''