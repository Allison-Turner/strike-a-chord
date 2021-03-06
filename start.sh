#!/bin/bash
ROLE=$1

MEMBER1IP=3.85.39.75
MEMBER1PORT=4020

MEMBER2IP=3.85.225.240
MEMBER2PORT=4021

MEMBER3IP=54.162.243.106
MEMBER3PORT=4022

MEMBER4IP=100.24.52.161
MEMBER4PORT=4023

MEMBER5IP=3.84.218.107
MEMBER5PORT=4024

MEMBER6IP=3.95.221.207
MEMBER6PORT=4025

MEMBER7IP=3.92.132.177
MEMBER7PORT=4026

MEMBER8IP=3.92.199.121
MEMBER8PORT=4027

javac Member.java MemberInfo.java Message.java ReceivingSocket.java RequestFile.java RequestFingerTable.java RequestSuccessor.java SendFile.java SendFingerTable.java SendingSocket.java

echo "Source compiled."

# I pass in all of the other machines because it's easier to just let the program cut the saved entries down by itself with addFingerTableEntry()

if [ $ROLE -eq 1 ]; then
  echo "Starting Member 1"
  java Member $MEMBER1PORT $MEMBER2IP $MEMBER2PORT $MEMBER3IP $MEMBER3PORT $MEMBER4IP $MEMBER4PORT $MEMBER5IP $MEMBER5PORT $MEMBER6IP $MEMBER6PORT $MEMBER7IP $MEMBER7PORT $MEMBER8IP $MEMBER8PORT
elif [ $ROLE -eq 2 ]; then
  echo "Starting Member 2"
  java Member $MEMBER2PORT $MEMBER1IP $MEMBER1PORT $MEMBER3IP $MEMBER3PORT $MEMBER4IP $MEMBER4PORT $MEMBER5IP $MEMBER5PORT $MEMBER6IP $MEMBER6PORT $MEMBER7IP $MEMBER7PORT $MEMBER8IP $MEMBER8PORT
elif [ $ROLE -eq 3 ]; then
  echo "Starting Member 3"
  java Member $MEMBER3PORT $MEMBER1IP $MEMBER1PORT $MEMBER2IP $MEMBER2PORT $MEMBER4IP $MEMBER4PORT $MEMBER5IP $MEMBER5PORT $MEMBER6IP $MEMBER6PORT $MEMBER7IP $MEMBER7PORT $MEMBER8IP $MEMBER8PORT
elif [ $ROLE -eq 4 ]; then
  echo "Starting Member 4"
  java Member $MEMBER4PORT $MEMBER1IP $MEMBER1PORT $MEMBER2IP $MEMBER2PORT $MEMBER3IP $MEMBER3PORT $MEMBER5IP $MEMBER5PORT $MEMBER6IP $MEMBER6PORT $MEMBER7IP $MEMBER7PORT $MEMBER8IP $MEMBER8PORT
elif [ $ROLE -eq 5 ]; then
  echo "Starting Member 5"
  java Member $MEMBER5PORT $MEMBER1IP $MEMBER1PORT $MEMBER2IP $MEMBER2PORT $MEMBER3IP $MEMBER3PORT $MEMBER4IP $MEMBER4PORT $MEMBER6IP $MEMBER6PORT $MEMBER7IP $MEMBER7PORT $MEMBER8IP $MEMBER8PORT
elif [ $ROLE -eq 6 ]; then
  echo "Starting Member 6"
  java Member $MEMBER6PORT $MEMBER1IP $MEMBER1PORT $MEMBER2IP $MEMBER2PORT $MEMBER3IP $MEMBER3PORT $MEMBER4IP $MEMBER4PORT $MEMBER5IP $MEMBER5PORT $MEMBER7IP $MEMBER7PORT $MEMBER8IP $MEMBER8PORT
elif [ $ROLE -eq 7 ]; then
  echo "Starting Member 7"
  java Member $MEMBER7PORT $MEMBER1IP $MEMBER1PORT $MEMBER2IP $MEMBER2PORT $MEMBER3IP $MEMBER3PORT $MEMBER4IP $MEMBER4PORT $MEMBER5IP $MEMBER5PORT $MEMBER6IP $MEMBER6PORT $MEMBER8IP $MEMBER8PORT
elif [ $ROLE -eq 8 ]; then
  echo "Starting Member 8"
  java Member $MEMBER8PORT $MEMBER1IP $MEMBER1PORT $MEMBER2IP $MEMBER2PORT $MEMBER3IP $MEMBER3PORT $MEMBER4IP $MEMBER4PORT $MEMBER5IP $MEMBER5PORT $MEMBER6IP $MEMBER6PORT $MEMBER7IP $MEMBER7PORT
else
  exit 1
fi
