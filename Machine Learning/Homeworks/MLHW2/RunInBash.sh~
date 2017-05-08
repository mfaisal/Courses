#!/bin/sh

outputFolder="/home/mustafa/workspace/MLHW2/src/numIterationResult/"

for (( c=10; c<=500; c+=10 ))
do
   if [ ! -d $outputFolder ]; then
		mkdir $outputFolder	
	fi
   outputFile=$outputFolder$c".txt"
   echo $outputFile

   java Main $c 0.01 2 0 > $outputFile
   
done

