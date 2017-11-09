#!/usr/bin/groovy

def call(String git_directory = '.') {

    env.JENKIS_SLVALE1 = 'flex1'
    env.JENKIS_SLVALE2 = 'flex0'

    script = "git --git-dir '${git_directory}/.git' config --get remote.origin.url | awk -F '/' '{print \$(NF-1)}'"
    env.GIT_PROJECT_KEY = sh(returnStdout: true, script: script).trim()

    def tokens = "${env.JOB_NAME}".tokenize('/')
    env.GIT_PROJECT = tokens[tokens.size()-3]
    env.GIT_REPO = tokens[tokens.size()-2]
    env.GIT_BRANCH = tokens[tokens.size()-1].replaceAll('%2F', '/')

    script = 'git show --name-only --pretty=format:"user:%ae" | grep user: || true'
    env.GIT_USER = sh(returnStdout: true, script: script).replaceAll("user:","").trim()


}