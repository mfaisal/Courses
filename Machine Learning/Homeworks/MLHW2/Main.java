import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Main {
	
	static double[] countTrainFiles = new double[2];  // count spam in countTrainFiles[0] and ham in countTrainFiles[1] 
	static double[] priorProbs = new double[2]; // probability for spam and ham in training data
	
	static ArrayList<String> wordList = new ArrayList<String>(); // total vocabulary in training data
	
	static Map<String,Double> spamTokenCounter = new HashMap<String,Double>(); // a dictionary for keeping token or word or term counter from spam files 
	static Map<String,Double> hamTokenCounter = new HashMap<String,Double>(); // a dictionary for keeping token or word or term counter from ham files
	
	static Map<String,Double> spamTokenProb = new HashMap<String,Double>(); // a dictionary for probability of term from spam files.
	static Map<String,Double> hamTokenProb = new HashMap<String,Double>(); // a dictionary for probability of term from ham files.
	
	static double spamFrequencies = 0; // total words in spam + number of attributes
	static double hamFrequencies = 0; // total words in ham + number of attributes
	
	static double weight_0=0.0;  // bias term
	static double[] weights;  // parameter for each attribute
	
	static double learning_rate = 0.01;  // learning rate
	static double strength_penalty = 10.0; // lamda for regularization
	
	static int iternation_num = 100; // number of iteration
	static ArrayList<Integer> trainingClass = new ArrayList<Integer>(); //class label of training data set.
	static Map<Integer, Map<String,Double>> trainingData = new HashMap<Integer, Map<String,Double>>(); // training data container <index,token list> 
	static int fileCounter = 0;
	static int[] converge;
	static double[] errors;
	static double weight_initial = 0.0;
	
	
	static ArrayList<String> wordListCopy = new ArrayList<String>(); // total vocabulary in training data
	static Map<String,Double> spamTokenCounterCopy = new HashMap<String,Double>(); // a dictionary for keeping token or word or term counter from spam files 
	static Map<String,Double> hamTokenCounterCopy = new HashMap<String,Double>(); // a dictionary for keeping token or word or term counter from ham files
	
	static Map<Integer, Map<String,Double>> trainingDataCopy = new HashMap<Integer, Map<String,Double>>(); // training data container <index,token list> 
   
	static double smoothConfident = 2.0;
	static double minWords=5.0;
	
	public static void main(String[] args){
		
		
		if(args.length>0){
			iternation_num = Integer.parseInt(args[0].trim());
			learning_rate = Double.parseDouble(args[1].trim());
			strength_penalty = Double.parseDouble(args[2].trim());
			weight_initial = Double.parseDouble(args[3].trim());
		}
		
		if(args.length>4){ // prior strength
			smoothConfident = Double.parseDouble(args[4].trim());
		}
		
		if(args.length>5){ // minimum words
			minWords = Double.parseDouble(args[5].trim());
		}
		
		File dataFile = new File("Data/train/spam/");
		getWordCounterPerClass(dataFile,spamTokenCounter); //allocating all words or token from Spam
		
		for(String word:spamTokenCounter.keySet()){ // add all spam tokens or words to a combined list
			if(!wordList.contains(word)){
				wordList.add(word);
				wordListCopy.add(word);
			}
			spamTokenCounterCopy.put(word, spamTokenCounter.get(word));
		}
		
		//System.out.println(spamTokenCounter.size());
		
		dataFile = new File("Data/train/ham/");
		getWordCounterPerClass(dataFile,hamTokenCounter); // allocating all words or token
		
		//System.out.println(hamTokenCounter.size());
		
		for(String word:hamTokenCounter.keySet()){ // add all ham tokens or words to a combined list
			if(!wordList.contains(word)){
				wordList.add(word);
				wordListCopy.add(word);
			}
			hamTokenCounterCopy.put(word, hamTokenCounter.get(word));
		}
		
		for(String word:wordList){  // make the spam and ham list in equal sized
			if(!spamTokenCounter.containsKey(word)){
				spamTokenCounter.put(word, 0.0);
				spamTokenCounterCopy.put(word, 0.0);
			}
			if(!hamTokenCounter.containsKey(word)){
				hamTokenCounter.put(word, 0.0);
				hamTokenCounterCopy.put(word, 0.0);
			}
		}
		
		priorProbs[0] = countTrainFiles[0]/(countTrainFiles[0]+countTrainFiles[1]); // calculate the probability of spam
		priorProbs[1] = countTrainFiles[1]/(countTrainFiles[0]+countTrainFiles[1]); // calculate the probability of ham
		
		System.out.println("*******************Normal Case*******************");
		
		updateStatistics(1.0); // for NB
		weightUpdate(); // for LR
		
		classify(); // classify without stop words
		
		System.out.println("------------Modifying smoothing---------");
		updateStatistics(smoothConfident); // for NB
		classify(); // classify without stop words
		updateStatistics(1.0); // for NB
		
		elminateStopWords(); // eliminate stop words from complete word list
		
		updateStatistics(1.0); // for NB after eliminating stop words.
		weightUpdate(); // for LR after eliminating stop words.
		
	
		System.out.println("+++++++++++++Stop words used:++++++++++++++");
		classify(); // classify without stop words.
		
		System.out.println(".........Eliminate less frequent word...........");
		eliminateFrequency(minWords);
		updateStatistics(1.0); // for NB
		weightUpdate(); // for LR
		
		classify(); // classify without stop words
		//System.out.println(wordList.size());
		
	}
	
	/**
	 * Update weights for LR
	 */
	private static void weightUpdate(){
		
		weight_0=weight_initial;
		weights = new double[wordList.size()];
		for(int i=0;i<weights.length;i++){
			weights[i] = weight_initial;
		}
		
		double lrSpenalty = 1.0-strength_penalty*learning_rate;
		
		errors = new double[trainingData.size()];
		converge = new int[wordList.size()+1];
		
		for(int i=1;i<=iternation_num;i++){
			
			double totalError = 0.0;
			
			for(int j=1;j<=trainingData.size();j++){
				double y_val=trainingClass.get(j-1); // training class spam = 0 and ham =1
				double predict_val = 0.0;
				Map<String,Double> doc = trainingData.get(j);
				
				for(String token:doc.keySet()){
					if(wordList.contains(token)){
						predict_val+= weights[wordList.indexOf(token)]*doc.get(token);
					}
				}
				
				predict_val += weight_0;
				predict_val = (1.0+Math.exp(predict_val));
				predict_val = 1.0/predict_val;
				errors[j-1] = y_val -(1.0 - predict_val); //error
				totalError += errors[j-1];
				
			}
			double pVal = weight_0 + learning_rate*totalError; // update w0
			if(pVal==weight_0){
				converge[0]=1;
			}else{
				weight_0 = pVal;
			}
			
			
			//update all the rest of weights
			for(int k=0;k<wordList.size();k++){
				double predict_val = 0;
				for(int j=0;j<errors.length;j++){
					if(trainingData.get(j+1).containsKey(wordList.get(k))){
						predict_val+= trainingData.get(j+1).get(wordList.get(k))*errors[j];
					}
				}
				
				pVal = weights[k]*lrSpenalty + learning_rate*predict_val; 
				
				if(pVal==weights[k]){
					converge[k+1] = 1;
				}else{
					weights[k]  =  pVal;
				}
				
			}
		}
	}
	
/**
 * Eliminate stop words
 */
	private static void elminateStopWords() {
		File dataFile;
		dataFile = new File("Data/stopWords.txt");
		
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			fr = new FileReader(dataFile.getAbsolutePath());
			br = new BufferedReader(fr);
			
			String line;
			while((line=br.readLine())!=null){
				
					line = line.trim();
					if(wordList.contains(line)){
						wordList.remove(line);
						spamTokenCounter.remove(line);
						hamTokenCounter.remove(line);
						spamTokenProb.remove(line);
						hamTokenProb.remove(line);
						
						for(int index:trainingData.keySet()){
							trainingData.get(index).remove(line);
						}
						
					}
			}
			
			fr.close();
			br.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void eliminateFrequency(double frequ){
		wordList = new ArrayList<String>();
		spamTokenCounter = new HashMap<String,Double>(); 
		hamTokenCounter = new HashMap<String,Double>();
		trainingData = new HashMap<Integer, Map<String,Double>>(); 
		
		wordList = wordListCopy;
		spamTokenCounter = spamTokenCounterCopy;
		hamTokenCounter = hamTokenCounterCopy;
		trainingData = trainingDataCopy;
		
		ArrayList<String> deletedWord = new ArrayList<String>();
		
		for(String word:wordList){
			if(spamTokenCounter.containsKey(word)){
				if(spamTokenCounter.get(word)<frequ){
					spamTokenCounter.remove(word);
					deletedWord.add(word);
				}
			}
			
			if(hamTokenCounter.containsKey(word)){
				if(hamTokenCounter.get(word)<frequ){
					hamTokenCounter.remove(word);
					deletedWord.add(word);
				}
			}
			
			for(int index:trainingData.keySet()){
				if(trainingData.get(index).containsKey(word)){
					if(trainingData.get(index).get(word)<frequ){
					trainingData.get(index).remove(word);
					}
				}
			}
		}
		
		for(String word:deletedWord){
			wordList.remove(word);
		}
		
		
		
	}
/**
 * update statics for LR after a change in data set.
 */
	private static void updateStatistics(double priorStrenth) {
		
		
		
		for(String word:spamTokenCounter.keySet()){
			spamFrequencies += spamTokenCounter.get(word);
		}
		
		spamFrequencies += (double)wordList.size()*priorStrenth;
		
		for(String word:hamTokenCounter.keySet()){
			hamFrequencies += hamTokenCounter.get(word);
		}
		
		hamFrequencies += (double)wordList.size()*priorStrenth;
		
		
		for(String word:spamTokenCounter.keySet()){
			double logVal = (spamTokenCounter.get(word)+priorStrenth)/spamFrequencies;
			spamTokenProb.put(word, logVal);
		}
		
		for(String word:hamTokenCounter.keySet()){
			double logVal = (hamTokenCounter.get(word)+priorStrenth)/hamFrequencies;
			hamTokenProb.put(word, logVal);
		}
	}
	
	/**
	 * Calculate score for each class using log likelihood
	 * @param listWord
	 * @param isSpam class -- for spam 0 and for ham 1
	 * @return score for specific class
	 */
	private static double getScore(ArrayList<String> listWord, int isSpam){
		double score = 0;
		
		if(isSpam==1){
			score += Math.log(priorProbs[0]);
		}else{
			score += Math.log(priorProbs[1]);
		}
		
		for(String word:listWord){
			if(isSpam==1){
				if(spamTokenProb.containsKey(word)){
					score += Math.log(spamTokenProb.get(word));
				}
			}else{
				if(hamTokenProb.containsKey(word)){
					score += Math.log(hamTokenProb.get(word));
				}
			}
		}
		
		return score;
	}
	/**
	 * Classification
	 */
	private static void classify(){
		File dataFile = new File("Data/test/");
		String absPath = dataFile.getAbsolutePath();
		
		double totalFiles = 0.0;
		double spamFiles = 0.0;
		double hamFiles = 0.0;
		
		double spamCounter = 0.0;
		double hamCounter = 0.0;
		
		double spamCounterLR = 0.0;
		double hamCounterLR = 0.0;
		
		for(String dir:dataFile.list()){
			
			String folderPath = absPath+"/"+dir+"/";
			File spamorhamDir = new File(folderPath);
			
			
			if(dir.equals("spam")){
				spamFiles = spamorhamDir.listFiles().length;
			}else{
				hamFiles = spamorhamDir.listFiles().length;
			}
			
			totalFiles += spamorhamDir.listFiles().length;
			
			for(File file:spamorhamDir.listFiles()){
				
				BufferedReader br = null;
				FileReader fr = null;
				ArrayList<String> wList = new ArrayList<String>();
				Map<String,Double> docContent = new HashMap<String,Double>();
				
				try {
					fr = new FileReader(file.getAbsolutePath());
					br = new BufferedReader(fr);
					
					String line;
					while((line=br.readLine())!=null){
						String[] words = line.split(" ");
						for(String word:words){
							if(!wList.contains(word)){
								wList.add(word);
							}
							
							if(docContent.containsKey(word)){
								Double db = docContent.get(word);
								docContent.put(word, db+1.0);
							}else{
								docContent.put(word, 1.0);
							}
						}
					}
					
					
					double predict_val =0;
					for(String token:docContent.keySet()){
						if(wordList.contains(token)){
							predict_val += weights[wordList.indexOf(token)]*docContent.get(token);
						}
					}
					
					predict_val = predict_val + weight_0;
					
					
					if(dir.equals("spam")){  // 0 for spam and 1 for ham
						if(getScore(wList,1)>getScore(wList,0)){
							spamCounter++;
						}
						if(predict_val<=0){
							spamCounterLR++;
						}
						
					}
					
					if(dir.equals("ham")){
						if(getScore(wList,0)>getScore(wList,1)){
							hamCounter++;
						}
						
						if(predict_val>0){
							hamCounterLR++;
						}
					}
					
					fr.close();
					br.close();
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		System.out.println("************Naive Bayes***************");
		System.out.println("True Spam: " + spamFiles + " Detected Spam: " + spamCounter + " Accuracy: " + spamCounter/spamFiles);
		System.out.println("True ham: " + hamFiles + " Detected ham: " + hamCounter + " Accuracy: " + hamCounter/hamFiles);
		
		System.out.println("Total Accuracy: " + (spamCounter+hamCounter)/totalFiles);
		
		System.out.println("************Logistic Regression***************");
		System.out.println("True Spam: " + spamFiles + " Detected Spam: " + spamCounterLR + " Accuracy: " + spamCounterLR/spamFiles);
		System.out.println("True ham: " + hamFiles + " Detected ham: " + hamCounterLR + " Accuracy: " + hamCounterLR/hamFiles);
		
		System.out.println("Total Accuracy: " + (spamCounterLR+hamCounterLR)/totalFiles);
		
	}
	/**
	 * For each spam or ham, count word frequency
	 * @param dataFile
	 * @param wordCounter
	 */
	private static void getWordCounterPerClass(File dataFile, Map<String, Double> wordCounter){
		
		
		
		if(dataFile.getName().equals("spam")){
			countTrainFiles[0] = dataFile.listFiles().length;
			for(int i=0;i<dataFile.listFiles().length;i++){
				trainingClass.add(0);
			}
			
		}else{
			countTrainFiles[1] = dataFile.listFiles().length;
			for(int i=0;i<dataFile.listFiles().length;i++){
				trainingClass.add(1);
			}
		}
		
		
		
		for(File file:dataFile.listFiles()){
			fileCounter++;
			
			BufferedReader br = null;
			FileReader fr = null;
			
			Map<String,Double> docContent = new HashMap<String,Double>();
			
			try {
				fr = new FileReader(file.getAbsolutePath());
				br = new BufferedReader(fr);
				String line;
				while((line=br.readLine())!=null){
					String[] words = line.split(" ");
					for(String word:words){
						if(wordCounter.containsKey(word)){
							Double db = wordCounter.get(word);
							wordCounter.put(word, db + 1.0);
						}else{
							wordCounter.put(word, 1.0);
						}
						
						if(docContent.containsKey(word)){
							Double db = docContent.get(word);
							docContent.put(word, db+1.0);
						}else{
							docContent.put(word, 1.0);
						}
						
					}
				}
				
				fr.close();
				br.close();
				
				trainingData.put(fileCounter, docContent);
				trainingDataCopy.put(fileCounter, docContent); // Alternative copy for later use.
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	

}
