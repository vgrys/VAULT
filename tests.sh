#!/usr/bin/env sh

py.test --junitxml tests/test_result/results.xml atf/tests/tempTest.py

#python atf/setup.py sdist