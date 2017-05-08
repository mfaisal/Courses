import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Mustafa Faisal
 *
 */
public class Perceptron {
static ArrayList<String> wordList = new ArrayList<String>();
	
	static ArrayList<Integer> trainingclasslabel = new ArrayList<Integer>();
	static Map<Integer, Map<String,Double>> trainingData = new HashMap<Integer, Map<String,Double>>();
	
	static ArrayList<Integer> testclasslabel = new ArrayList<Integer>();
	static Map<Integer, Map<String,Double>> testData = new HashMap<Integer, Map<String,Double>>();
	
	static double[] weights; 
	
	static double learning_rate = 0.01;  // learning rate
	static int iternation_num = 1000; // number of iteration
	static int[] converge;
	static double weight_initial = 0.1;
	
	static String trainingDir = "Data/train/";
	static String testDir = "Data/test/";
	static String stopWordFilePath = "Data/stopWords.txt";
	
	public static void main(String[] args){
		
		if(args.length>0){
			trainingDir = args[0].trim();  // training data set folder
			testDir = args[1].trim(); // test data set folder
			stopWordFilePath = args[2].trim(); // stop word file
			iternation_num = Integer.parseInt(args[3].trim()); // iteration number
			learning_rate = Double.parseDouble(args[4].trim()); // learning rate
			weight_initial = Double.parseDouble(args[5].trim());// initial wegit
		}
		
		File dataFile = new File(trainingDir);
		gatherData(dataFile);
		
		dataFile = new File(testDir);
		gatherData(dataFile);
		
		initialization();
		training();
		
		classify();
		
		elminateStopWords();
		
		initialization();
		training();
		
		System.out.println("***********After Stop Word Elemination***********");
		classify();
		
	}
	/**
	 * 
	 */
	
	static void training(){
		for(int iteration=1;iteration<=iternation_num;iteration++){
			for(int index:trainingData.keySet()){
				Map<String, Double> doc = trainingData.get(index);
				
				int pre_val = predict(doc);
				
				int error = trainingclasslabel.get(index-1)-pre_val;
				
				if(error!=0){ // weight update is required.
					for(String word:doc.keySet()){
						if(wordList.contains(word)){
							weights[wordList.indexOf(word)] += learning_rate*error*doc.get(word);
						}
					}
					
					weights[wordList.size()] += learning_rate*error;
				}
			}
		}
	}
	
	static void classify(){
		
		double correctHam = 0.0;
		double correctSpam = 0.0;
		double detectedHam = 0.0;
		double detectedSpam = 0.0;
		
		for(int index:testData.keySet()){
			Map<String, Double> doc = testData.get(index);
			
			if(testclasslabel.get(index-1)==1){
				correctHam++;
			}
			
			if(testclasslabel.get(index-1)==-1){
				correctSpam++;
			}
			double sum=0.0;
			for(String word:doc.keySet()){
				if(wordList.contains(word)){
					sum = sum + weights[wordList.indexOf(word)]*doc.get(word);
				}
			}
			sum = sum + weights[wordList.size()];
			int predt = (sum>0.0)?1:-1;
			
			if(testclasslabel.get(index-1)==1&&predt==testclasslabel.get(index-1)){
				detectedHam++;
			}
			
			if(testclasslabel.get(index-1)==-1&&predt==testclasslabel.get(index-1)){
				detectedSpam++;
			}
			
		}
		
		System.out.println("Total Ham:" + correctHam + " Detected Ham: " + detectedHam + " Accuracy: " + (double)(detectedHam/correctHam));
		System.out.println("Total Spam:" + correctSpam + " Detected Spam: " + detectedSpam + " Accuracy: " + (double)(detectedSpam/correctSpam));
		System.out.println("Overall Accuracy: " + (double)((detectedHam+detectedSpam)/(correctHam+correctSpam)));
	}
	
	static int predict(Map<String,Double> doc ){
		double sum = 0.0;
		
		for(String word:doc.keySet()){
			if(wordList.contains(word)){
				sum = sum + weights[wordList.indexOf(word)]*doc.get(word);
			}
		}
		sum = sum + weights[wordList.size()]; // adding bias term
		
		if(sum>0.0){
			return 1;
		}
		else
			return -1;
		
	}
	
	
	private static void elminateStopWords() {
		
		File dataFile = new File(stopWordFilePath);
		
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
					}
			}
			
			fr.close();
			br.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void initialization(){
		weights = new double[wordList.size()+1]; // bias term is last one
		
		for(int i=0;i<weights.length;i++){
			weights[i] = weight_initial;
		}
	}
	
	static void gatherData(File dataFile){
		ArrayList<String> forbiddenStrings = new ArrayList<String>();
		forbiddenStrings.add(",");
		forbiddenStrings.add("'");
		forbiddenStrings.add("\"");
		forbiddenStrings.add("\\");
		forbiddenStrings.add("\n");
		String absPath = dataFile.getAbsolutePath();
		int fCounter = 0;
		for(String dir:dataFile.list()){
			String folderPath = absPath+"/"+dir+"/";
			File spamorhamDir = new File(folderPath);
			
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
								double db = docContent.get(word);
								docContent.put(word, db+1.0);
							}else{
								docContent.put(word, 1.0);
							}
						}
					}
					
					fCounter++;
					
					if(dataFile.getName().contains("train")){
						for(String token:docContent.keySet()){
							if(!wordList.contains(token.trim())&&!forbiddenStrings.contains(token.trim())){
								wordList.add(token);
							}
						}
						
						if(dir.equals("spam")){  // -1 for spam and 1 for ham
							trainingclasslabel.add(-1);
						}
						
						if(dir.equals("ham")){
							trainingclasslabel.add(1);
						}
						
						trainingData.put(fCounter, docContent);
						
					}
					if(dataFile.getName().contains("test")){
						if(dir.equals("spam")){  // -1 for spam and 1 for ham
							testclasslabel.add(-1);
						}
						
						if(dir.equals("ham")){
							testclasslabel.add(1);
						}
						
						testData.put(fCounter, docContent);
					}
					
					fr.close();
					br.close();
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
}
