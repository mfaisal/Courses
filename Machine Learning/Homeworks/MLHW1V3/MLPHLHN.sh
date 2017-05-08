outputFolder="/home/mustafa/workspace/MLHW3/src/Output/enron4/MLP/HN/"

trainingfile="/home/mustafa/workspace/MLHW3/Arff/enron4/train.arff"
testfile="/home/mustafa/workspace/MLHW3/Arff/enron4/test.arff"

iteration=(1 2 3 4 5 10 15 20)

for i in ${iteration[@]}
do
     outputFile=$outputFolder$i".txt"
     java -cp weka.jar weka.classifiers.functions.MultilayerPerceptron -L 0.01 -M 0.1 -N 600 -V 0 -S 0 -E 20 -H 2,$i -t $trainingfile -T $testfile > $outputFile
done
