#!/usr/bin/env bash

set -x

{
    ls -l
    pwd
    testttt
} 2>&1 | tee -a $DEBUGLOG