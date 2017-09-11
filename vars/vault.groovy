#!/usr/bin/groovy
// @Grapes(
//     @Grab(group='com.bettercloud', module='vault-java-driver', version='3.0.0')
// )


@Grab('org.apache.commons:commons-math3:3.4.1') import org.apache.commons.math3.primes.Primes
class Lib {
  static boolean isPrime(int n) {Primes.isPrime(n)}
}

def call(body) {

    def config = [:]
    body()
    println("\nHello World")
    println("\nTesting Library")
}
