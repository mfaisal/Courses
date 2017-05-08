outputFolder="/home/mustafa/workspace/MLHW3/src/Output/enron4/MLP/Momemtum/largerIteration/"

trainingfile="/home/mustafa/workspace/MLHW3/Arff/enron4/train.arff"
testfile="/home/mustafa/workspace/MLHW3/Arff/enron4/test.arff"

iteration=(0 0.01 0.05 0.1 0.3 0.5 0.8 1.0)

for i in ${iteration[@]}
do
     outputFile=$outputFolder$i".txt"
     java -cp weka.jar weka.classifiers.functions.MultilayerPerceptron -L 0.01 -M $i -N 600 -V 0 -S 0 -E 20 -H 1 -t $trainingfile -T $testfile > $outputFile
done
