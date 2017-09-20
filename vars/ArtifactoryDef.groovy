#!/usr/bin/groovy
package com.epam



    def quotes = ['Make it so', 'Tea. Earl Grey. Hot.', 'Engage']
    println quotes.get(new Random().nextInt(quotes.size()))


    def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())


