#!/usr/bin/groovy

def call() {

    env.JENKIS_SLVALE1 = 'flex1'
    env.JENKIS_SLVALE2 = 'flex0'
    env.TDM_SERVER_QA = "fdg"


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