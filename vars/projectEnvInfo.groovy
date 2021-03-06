#!/usr/bin/groovy

def call() {


    env.JENKIS_SLVALE1 = 'flex1'
    env.JENKIS_SLVALE2 = 'flex0'
    env.TDM_SERVER_QA = "fdg"

    env.SSH_KEY = credentials('SSH-KEY')
    env.SSH_ANSIBLE = credentials('SSH-ANSIBLE')
    env.VAULT_TOKEN = credentials('VAULT_TOKEN')

    withCredentials([usernamePassword(credentialsId: 'arifactoryID', usernameVariable: 'env.SAUCE_ACCESS_USR', passwordVariable: 'env.SAUCE_ACCESS_PWD')]){
        }

    withCredentials([file(credentialsId: "JEN-KEY-NOPWD", variable: "env.JenKeyNopwd")]){

    }

    withCredentials([string(credentialsId: 'VAULT_TOKEN', variable: 'MY_VAULT_TOKEN')]){

    }

    echo pipelineConfig.pad("Environments are set")

//    String dssNode = 'q9lcwptdmci01.labcorp.ad.ctc'
//    String sshKeyId = '7e503b79-703a-489b-9a56-b1369b36b417'
//
//    String targetHostUser = 'svc.cicdbigdata'
//    GString targetHost = "${targetHostUser}@d9lcwphd1e1"
//
//    String tdmServer = 'q9lcwptdmap01.labcorp.ad.ctc'



//    script = 'git show --name-only --pretty=format:"user:%ae" | grep user: || true'
//    env.GIT_USER = sh(returnStdout: true, script: script).replaceAll("user:","").trim()


}