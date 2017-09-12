#!/usr/bin/groovy

// @Grapes(
//     @Grab(group='com.bettercloud', module='vault-java-driver', version='3.0.0')
// )

import com.epam.vaultCommands
@Grab('org.apache.commons:commons-math3:3.4.1')
import org.apache.commons.math3.primes.Primes

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
    new vaultCommands().print_command(11)
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

class vaultCommands implements Serializable {
    def print_command(int n) {

//      def config = [:]
//       body()
        println("Hello World")
        println(Primes.isPrime(n))
    }
}