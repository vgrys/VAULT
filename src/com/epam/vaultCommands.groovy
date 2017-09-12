#!/usr/bin/groovy

package com.epam


@Grab('org.apache.commons:commons-math3:3.4.1')
//@GrabConfig(systemClassLoader=true)

import org.apache.commons.math3.primes.Primes

class vaultCommands implements Serializable {
    def print_command(int n) {

//        ServiceLoader<Primes> loader = ServiceLoader.load(Primes.class);
        println("Hello World")
        println(Primes.isPrime(n))
    }
}
