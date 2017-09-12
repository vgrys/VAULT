#!/usr/bin/groovy

// @Grapes(
//     @Grab(group='com.bettercloud', module='vault-java-driver', version='3.0.0')
// )

import com.epam.vaultCommands

//def call(body) {
//
//    body()
//    def vc = new vaultCommands()
//    vc.print_command(11)
//    echo "done"
//}

def call(body) {
    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
        def vc = new vaultCommands()
    vc.print_command(11)
    echo "done"

//    def flow = new TerradatumCommands()

//    Version version = config.version
//
//    Version newVersion = version
//
//    echo "Tagged version: ${newVersion}"
//
//    return newVersion
}