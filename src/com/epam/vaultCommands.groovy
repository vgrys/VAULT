#!/usr/bin/groovy


@Grab('org.apache.commons:commons-math3:3.4.1') import org.apache.commons.math3.primes.Primes

class vaultCommands implements Serializable, Comparable {
def print_command(int n) {

    def config = [:]
    // body()
    println("\n$USER")
    println(Primes.isPrime(n))
}
}
