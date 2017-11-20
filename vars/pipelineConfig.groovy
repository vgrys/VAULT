#!/usr/bin/groovy

def pad(str) {
    return "********** ${str} ***********"
}

def beginning() {
//    currentBuild.result = "SUCCESS"
    echo "DEBUG CODE -----> Running ${env.JOB_NAME} on ${env.JENKINS_URL} for branch ${env.BRANCH_NAME}"
}

def createProjectBundle(sourceFolder) {
    def zip = new ZipTools()
    def bundlePath = zip.bundle(env, sourceFolder, ['.git', '.gitignore'])
    echo "created an archive $bundlePath"
}

def projectName () {
    def conf = SharedConfiguration.get()
    return projectName = conf.projectName
}

def runATFCommand(targetHost, user, projectName, command) {
    String commandToRun = "cd /home/${user}/${projectName}; source ./ATFVENV/bin/activate; ${command}"
    sh sshCli(targetHost, commandToRun)
}

def sshCli(host, commandToRun) {
    return "ssh -o StrictHostKeyChecking=no ${host} /bin/bash -c '\"${commandToRun}\"'"
}

def executeAnsible(ansibleCommand, artifactoryId) {
    withCredentials([usernamePassword(credentialsId: "${artifactoryId}", usernameVariable: 'artifactory_user', passwordVariable: 'artifactory_pwd')]) {
        dir("${WORKSPACE}/ansible") {
            sh ansibleCommand
        }
    }
}

def reportGitParams() {
    echo "Git Origin: ${env.GIT_ORIGIN}, Git User: ${env.GIT_USER}, Git Project: ${env.GIT_PROJECT}, Git Branch: ${env.GIT_BRANCH}, Git Repo: ${env.GIT_REPO}, Git Feature Name (optional): ${env.GIT_FEATURE_NAME}"
}

def static ansible(command, targetGroup) {
    return "ansible-playbook --limit ${targetGroup} --extra-vars 'server=${targetGroup} user=artifactory_user password=artifactory_pwd ${command}"
}

def runDeployATF(String artifactoryUrl, String artifactoryRepo, String atfVersion, String release, String projectName, String targetGroup) {
    withCredentials([usernamePassword(credentialsId: "${artifactoryID}", usernameVariable: 'artifactory_user', passwordVariable: 'artifactory_pwd')]) {
        withCredentials([file(credentialsId: 'zeph', variable: 'zephCred')]) {
            dir("${env.WORKSPACE}/ansible") {
                sh ansible("artifactoryUrl=${artifactoryUrl} artifactoryRepo=${artifactoryRepo} atfVersion=${atfVersion} atfRelease=${release} projectName=${projectName} zephCred=${zephCred}' ATFDeployment.yml", targetGroup)
            }
        }
    }
}

def runDeployProject(artifactoryUrl, artifactoryRepo, projectName, projectArchiveName, targetGroup, artifactoryId) {
    def cmd = ansible("artifactoryUrl=${artifactoryUrl} artifactoryRepo=${artifactoryRepo} projectName=${projectName} projectArchiveName=$projectArchiveName' projectDeployment.yml", targetGroup)
    executeAnsible(cmd, artifactoryId)
}

def runProjectCleanup(projectName, targetGroup ) {
    cmd = ansible("projectName=${projectName}' projectCleanup.yml", targetGroup)
    executeAnsible(cmd, null)
}
