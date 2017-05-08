#!/bin/sh

outputFolder="/home/mustafa/workspace/MLHW2/src/initialWeight/"

for (( c=-4; c<=2; c++ ))
do
   if [ ! -d $outputFolder ]; then
		mkdir $outputFolder	
	fi
   outputFile=$outputFolder$c".txt"
   echo $outputFile

   java Main 280 0.01 2 $c > $outputFile
   
done
