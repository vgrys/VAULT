#!/usr/bin/groovy
package com.epam


def call() {
    def quotes = ['Make it so', 'Tea. Earl Grey. Hot.', 'Engage']
    println quotes.get(new Random().nextInt(quotes.size()))
}