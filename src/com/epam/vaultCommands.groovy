#!/usr/bin/groovy

package com.epam
import org.apache.commons.math3.primes.Primes

@Grab('org.apache.commons:commons-math3:3.4.1')
//@GrabConfig(systemClassLoader=true)



class vaultCommands implements Serializable {
    def print_command(int n) {

//        ServiceLoader<Primes> loader = ServiceLoader.load(Primes.class);
        println "Hello World"
        script.sh "echo 1"
//        error Primes.isPrime(n)
    }
}
