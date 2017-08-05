#!/usr/bin/env bash

MEMORY="-Xms512m -Xmx512m -XX:MaxMetaspaceSize=256m"
GC1="-XX:+UseSerialGC -XX:+UseSerialGC"
GC2="-XX:+UseParallelGC -XX:+UseParallelOldGC"
GC3="-XX:+UseParNewGC -XX:+UseConcMarkSweepGC"
GC4="-XX:+UseG1GC"

java $MEMORY $GC1 -jar target/HW4.jar > results/GC1.out
java $MEMORY $GC2 -jar target/HW4.jar > results/GC2.out
java $MEMORY $GC3 -jar target/HW4.jar > results/GC3.out
java $MEMORY $GC4 -jar target/HW4.jar > results/GC4.out