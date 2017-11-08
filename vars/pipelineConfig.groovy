#!/usr/bin/groovy

def pad(str) {
    return "********** ${str} ***********"
}

def createProjectBundle(sourceFolder) {
    def zip = new ZipTools()
    def bundlePath = zip.bundle(env, sourceFolder, ['.git', '.gitignore'])
    echo "created an archive $bundlePath"
}

def runATFCommand(targetHost, user, projectName, command) {
    String commandToRun = "cd /home/${user}/${projectName}; source ./ATFVENV/bin/activate; ${command}"
    sh sshCli(targetHost, commandToRun)
}

def sshCli(host, commandToRun) {
    return "ssh -o StrictHostKeyChecking=no ${host} /bin/bash -c '\"${commandToRun}\"'"
}

def executeAnsible(ansibleCommand) {
    withCredentials([usernamePassword(credentialsId: 'arifactoryID', usernameVariable: 'artifactory_user', passwordVariable: 'artifactory_pwd')]) {
        dir("${WORKSPACE}/ansible") {
            sh ansibleCommand
        }
    }
}

def reportGitParams() {
    echo "Git Origin: ${env.GIT_ORIGIN}, Git User: ${env.GIT_USER}, Git Project: ${env.GIT_PROJECT}, Git Branch: ${env.GIT_BRANCH}, Git Repo: ${env.GIT_REPO}, Git Feature Name (optional): ${env.GIT_FEATURE_NAME}"
}

def static ansible(command, targetGroup) {
    return "ansible-playbook --extra-vars 'server=${targetGroup} user=artifactory_user password=artifactory_pwd ${command}"
}

def runDeployATF(String artifactoryRepo, String artifactoryUrl, String atfVersion, String release, String projectName, String targetGroup) {
    withCredentials([usernamePassword(credentialsId: 'arifactoryID', usernameVariable: 'artifactory_user', passwordVariable: 'artifactory_pwd')]) {
        sh "cp ${env.WORKSPACE}/requirements.txt ${env.WORKSPACE}/requirements-new.txt"
        withCredentials([file(credentialsId: 'zeph', variable: 'zephCred')]) {
            dir("${env.WORKSPACE}/ansible") {
                sh ansible("artifactoryRepo=${artifactoryRepo} artifactoryUrl=${artifactoryUrl} atfVersion=${atfVersion} atfRelease=${release} projectName=${projectName} workspace=${WORKSPACE} zephCred=${zephCred}' ATFDeployment.yml", targetGroup)
            }
        }
    }
}

def runDeployProject(artifactoryUrl, artifactoryRepo, projectName, projectArchiveName, targetGroup) {
    def cmd = ansible("artifactoryUrl=${artifactoryUrl} artifactoryRepo=${artifactoryRepo} projectName=${projectName} projectArchiveName=$projectArchiveName' projectDeployment.yml", targetGroup)
    executeAnsible(cmd)
}

def runProjectCleanup(projectName, targetGroup ) {
    cmd = ansible("projectName=${projectName}' projectCleanup.yml", targetGroup)
    executeAnsible(cmd)
}
