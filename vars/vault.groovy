#!/usr/bin/groovy
// @Grapes(
//     @Grab(group='com.bettercloud', module='vault-java-driver', version='3.0.0')
// )

import com.epam.vaultCommands

def call(body) {

    body()
    def vc = new vaultCommands()
    vc.print_command(11)
    echo "done"
}
