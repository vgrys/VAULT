#!/usr/bin/groovy

def call(String git_directory = '.') {

    script = "git --git-dir '${git_directory}/.git' config --get remote.origin.url | awk -F '/' '{print \$(NF-1)}'"
    env.GIT_PROJECT_KEY = sh(returnStdout: true, script: script).trim()

    def tokens = "${env.JOB_NAME}".tokenize('/')
//    env.GIT_PROJECT = tokens[tokens.size()-3]
//    env.GIT_REPO = tokens[tokens.size()-2]
//    env.GIT_BRANCH = tokens[tokens.size()-1].replaceAll('%2F', '/')

    script = 'git show --name-only --pretty=format:"user:%ae" | grep user: || true'
    env.GIT_USER = sh(returnStdout: true, script: script).replaceAll("user:","").trim()

    script = 'git remote -v | grep push || true'
    env.GIT_ORIGIN = sh(returnStdout: true, script: script).replaceAll(" \\(push\\)","").replaceAll("origin	https://","").replaceAll("origin	ssh://git@","").replaceAll("/scm/",":22/")

    if (env.BRANCH_NAME.contains('feature/')) {
        env.GIT_FEATURE_NAME = env.GIT_BRANCH.replaceAll('feature/', '')
        env.GIT_BRANCH_TYPE = "feature"
    } else if (env.BRANCH_NAME.contains('release/')) {
        env.GIT_BRANCH_TYPE = "release"
        env.GIT_FEATURE_NAME = ""
    } else if (env.BRANCH_NAME.contains('develop')) {
        env.GIT_BRANCH_TYPE = "develop"
        env.GIT_FEATURE_NAME = ""
    } else if (env.BRANCH_NAME.contains('bugfix')) {
        env.GIT_BRANCH_TYPE = "bugfix"
        env.GIT_FEATURE_NAME = ""
    } else if (env.BRANCH_NAME.contains('PR-')) {
        env.GIT_BRANCH_TYPE = "PR"
        env.GIT_FEATURE_NAME = ""
    } else if (env.BRANCH_NAME == "master") {
        env.GIT_BRANCH_TYPE = "master"
        env.GIT_FEATURE_NAME = ""
    }
}