#!/usr/bin/groovy
// @Grapes(
//     @Grab(group='com.bettercloud', module='vault-java-driver', version='3.0.0')
// )

import com.epam.vaultCommands

def call(body) {

    def config = [:]
    body()
    vaultCoomands.print_command(11)
}
