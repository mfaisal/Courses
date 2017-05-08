import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Mustafa Faisal
 *
 */
public class ColaborativeFilter {
	
	static Map<Integer, Map<Integer,Double>> trainingData = new HashMap<Integer, Map<Integer,Double>>(); // user1 --> item1 ---> rating
																										//        --> item2 ---> rating
	static Map<Integer, Map<Integer,Double>> testData = new HashMap<Integer, Map<Integer,Double>>(); 
	
	static Map<Integer, Double> userAverage = new HashMap<Integer, Double>(); // user ---> average vote
	
	static String trainingFileName = "netflix/TrainingRatings.txt";
	static String testFileName = "netflix/TestingRatings.txt";
	public static void main(String[] args){
		
		if(args.length>0){
			trainingFileName = args[0].trim(); // training file 
			testFileName = args[1].trim(); // test file
		}
		
		File dataFile = new File(trainingFileName);
		readData(dataFile, trainingData);
		calculateAverageRatingPerUser();
		//System.out.println(trainingData.size());
		
		dataFile = new File(testFileName);
		readData(dataFile, testData);
		
		
		
		predictRatings();
		
		
		
	}
/**
 * 
 */
	private static void predictRatings() {
		try {
			double absoluteError = 0.0;
			double meanSquareError = 0.0;
			double itemCounter = 0.0;
			
			File writeFile = new File("PredictedRating.txt");
	 		if(!writeFile.exists()){
	 			
					writeFile.createNewFile();
				
	 		}
	 		else{
	 			writeFile.delete();
	 		}
			
			FileWriter fw = new FileWriter(writeFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			double predict;
			double normalizingFactor;
			double weightdiff;
			
			for(int activeUser:testData.keySet()){
				
				predict = userAverage.get(activeUser);
				
				Map<Integer,Double> otherusers = calculateCorelation(activeUser);
				
				for(int movie:testData.get(activeUser).keySet()){
					
					normalizingFactor = 0.0;
					weightdiff = 0.0;
					
					for(int user:otherusers.keySet()){
						if(otherusers.get(user)!=0.0){
							normalizingFactor+=Math.abs(otherusers.get(user));
							if(trainingData.get(user).containsKey(movie)){
								weightdiff+= otherusers.get(user)*(trainingData.get(user).get(movie) - userAverage.get(user));
							}
						}
					}
					if(normalizingFactor!=0.0){
						predict = predict + weightdiff/normalizingFactor; // for some cases second term negative and higher than first term
						// This makes prediction negative which is not feasible.
					}
					
					bw.write(movie + "," + activeUser + "," +predict + "," + testData.get(activeUser).get(movie) +"\n");
					
					absoluteError += Math.abs(testData.get(activeUser).get(movie)-predict);
					meanSquareError += (testData.get(activeUser).get(movie)-predict)*(testData.get(activeUser).get(movie)-predict);
					itemCounter++;
				}
				
			}
			
			System.out.println("Mean Absolute Error: " + absoluteError/itemCounter);
			System.out.println("Root Mean Squared Error: " + meanSquareError/itemCounter);
			
			bw.write("Mean Absolute Error: " + absoluteError/itemCounter+"\n");
			bw.write("Root Mean Squared Error: " + meanSquareError/itemCounter+"\n");
			
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Calculate weights or correlation or pearson similarity  for other users of each user.
	 * @param activeuser given user
	 * @return dictionary of similarities for other users.
	 */
	static Map<Integer,Double> calculateCorelation(int activeuser){
		double numerator;
		double denumerator1;
		double denumerator2;
		
		double activediffFromAvg;
		double currentdiffFromAvg;
			
		Map<Integer,Double> activeUserMovies = trainingData.get(activeuser);
		Map<Integer,Double> otherUser = new HashMap<Integer,Double>();
		
		for(int trainUser:trainingData.keySet()){
			
			numerator = 0.0;
			denumerator1 = 0.0;
			denumerator2 = 0.0;
				
			for(int actMovie:activeUserMovies.keySet()){
					if(trainingData.get(trainUser).containsKey(actMovie)){
						activediffFromAvg =  trainingData.get(activeuser).get(actMovie) - userAverage.get(activeuser);
						currentdiffFromAvg = trainingData.get(trainUser).get(actMovie) - userAverage.get(trainUser);
						numerator = numerator + activediffFromAvg*currentdiffFromAvg;
						denumerator1 = denumerator1 + activediffFromAvg*activediffFromAvg;
						denumerator2 = denumerator2 + currentdiffFromAvg*currentdiffFromAvg;
					}
			
			}
			if(denumerator1 != 0.0&&denumerator2 != 0.0 &&numerator!=0.0){
				otherUser.put(trainUser, numerator/Math.sqrt(denumerator1*denumerator2));
			}
		}
		return otherUser;
			
	}
	/**
	 * Calculate average ratings per user
	 */
	static void calculateAverageRatingPerUser(){
		for(int user:trainingData.keySet()){
			Map<Integer,Double> movies = trainingData.get(user);
			double sum = 0;
			for(int movie:movies.keySet()){
				sum+=movies.get(movie);
			}
			sum = sum/(double)movies.size();
			userAverage.put(user, sum);
		}
	}
	/**
	 * This method read file and allocate data into a dictionary
	 * @param dataFile --> data file  
	 * @param dataset  --> dictionary
	 */
	static void readData(File dataFile, Map<Integer, Map<Integer,Double>> dataset){
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			fr = new FileReader(dataFile.getAbsolutePath());
			br = new BufferedReader(fr);
			
			String line;
			while((line=br.readLine())!=null){
				String[] fields = line.split(",");
				
				int user = Integer.parseInt(fields[1]);
				int movie = Integer.parseInt(fields[0]);
				double rating = Double.parseDouble(fields[2]);
				
				if(!dataset.containsKey(user)){
					Map<Integer,Double> movRating = new HashMap<Integer,Double>();
					movRating.put(movie, rating);
					dataset.put(user, movRating);
				}else{
					dataset.get(user).put(movie, rating);
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
