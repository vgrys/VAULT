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

def ansible(command, targetGroup) {
    return "ansible-playbook --extra-vars 'server=${targetGroup} user=artifactory_user password=artifactory_pwd ${command}"
}

def runDeployATF(String artifactoryRepo, String artifactoryUrl, String atfVersion, String projectName) {
    withCredentials([usernamePassword(credentialsId: 'artifactoryIDVG', usernameVariable: 'artifactory_user', passwordVariable: 'artifactory_pwd')]) {
        sh "cp $WORKSPACE/ci-cd-framework/requirements.txt $WORKSPACE/requirements.txt"
        withCredentials([file(credentialsId: 'atf-config', variable: 'atfConf')]) {
            dir("${WORKSPACE}/ci-cd-framework/ansible") {
                sh ansible("artifactoryRepo=${artifactoryRepo} artifactoryUrl=${artifactoryUrl} atfVersion=${atfVersion} projectName=${projectName} workspace=${WORKSPACE} atfConf=${atfConf}' ATFDeployment.yml")
            }
        }
    }
}

def runDeployProject(artifactoryUrl, artifactoryRepo, projectVersion, projectName) {
    def cmd = ansible("artifactoryUrl=${artifactoryUrl} artifactoryRepo=${artifactoryRepo} projectVersion=${projectVersion} projectName=${projectName} workspace=${WORKSPACE}' projectDeployment.yml")
    executeAnsible(cmd, targetGroup )
}

def runProjectCleanup(projectName, targetGroup ) {
    cmd = pipelineConfig.ansible("projectName=${projectName}' projectCleanup.yml", targetGroup)
    executeAnsible(cmd)
}