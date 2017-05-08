#!/bin/sh

outputFolder="/home/mustafa/workspace/MLHW2/src/learningRate/"

lrates=(0 0.01 0.02 0.03 0.04 0.05 0.06 0.07 0.08 0.09 0.1)
for c in ${lrates[@]}; 
do
   if [ ! -d $outputFolder ]; then
		mkdir $outputFolder	
	fi
   outputFile=$outputFolder$c".txt"
   echo $outputFile

   java Main 280 $c 2 0 > $outputFile
   
done
