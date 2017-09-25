#!/usr/bin/env sh

py.test --junitxml results.xml atf/tests/tempTest.py

#python atf/setup.py sdist