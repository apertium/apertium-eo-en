#!/bin/bash

# Check for <sint> consistency

lt-expand apertium-eo-en.en.dix | grep '<sint>' | cut -f2 -d':' | cut -f1 -d'<' | uniq -u > /tmp/sintadj2
n=0;

for line in `cat apertium-eo-en.eo-en.dix |sed 's/ /_/g'`; 
do
  EN=`echo $line | sed 's/<r>/@/g' | cut -f2 -d'@' | cut -f1 -d'<'`; 
#  n=$((n+1))
  echo $line | grep '<s_n="adj"/>' > /dev/null; 
  if [ $? -eq 0 ]; 
  then 
#    echo $line;
    cat /tmp/sintadj2 | grep $EN >/dev/null; 
    if [ $? -eq 0 ]; 
    then
      echo $line | grep '<s_n="sint"/>' > /dev/null; 
      if [ $? -ne 0 ]; 
      then
#        echo Add '<s n="sint">' to $EN  in line $n: $line
        echo Add '<s n="sint">' to $EN  in $line
      fi
    else
      echo $line | grep '<s_n="sint"/>' > /dev/null; 
      if [ $? -eq 0 ]; 
      then
        echo Remove '<s n="sint">' to $EN  in $line
      fi
    fi
  fi
done


