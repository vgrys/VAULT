#!/usr/bin/groovy

package com.epam

@Grab('org.apache.commons:commons-math3:3.4.1')
import org.apache.commons.math3.primes.Primes
import java.util.*
import java.io.*

class vaultCommands implements Serializable {
    def print_command(int n) {

//      def config = [:]
//       body()
        println("Hello World")
        println(Primes.isPrime(n))
    }
}
