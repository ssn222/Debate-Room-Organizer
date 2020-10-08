import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Semester {
	
	// Configuration rating constants
	private static double POSITION_RATING_MULTIPLIER = 1.0;
	private static double PARTNER_RATING_MULTIPLIER = 0.75;
	private static double DECAY_RATE = 0.65;
	private static double INITIAL_RATING_THRESHOLD = 0;
	private static double THRESHOLD_GROWTH_RATE = 1.25;
	private static double MAX_NUM_CONFIGS_TO_TEST = 5000;
	
	private Random rand = new Random();
	private int numWeeks = 12;
	private int numDebaters;
	private Debater[] competitors;
	private ArrayList<ArrayList<Integer>> configs = new ArrayList<ArrayList<Integer>>();

	
	public Semester(String[] names){
		numDebaters = names.length;
		competitors = new Debater[numDebaters];
		
		for (int i = 0; i < numDebaters; i++) {
			competitors[i] = new Debater(names[i]);
			competitors[i].setID(i);
		}
	}
	
	public int getNumberOfWeeksConfigured(){
		int weeks = -1;
		if(configs.get(0) != null){
			weeks++;
			while(configs.get(weeks) != null){
				weeks++;
			}
		}
		
		return weeks;
	}
	
	public ArrayList<Integer> getConfigByWeek(int week){
		return configs.get(week);
	}
	
	public String getDebaterByID(int ID){
		return competitors[ID].getName();
	}
	
	public void commitConfig(int week, ArrayList<Integer> config){
		
		for (int i = 0; i < config.size(); i++) {
			// Sets team
			int currentDebaterID = config.get(i);
			competitors[currentDebaterID].setTeam(week, getPositionFromConfiguration(i));
			
			// Sets partner
			competitors[currentDebaterID].setPartner(week, getPartnerFromConfiguration(i, config));
		}
		
		configs.add(week, config);
	}
	
	public int getNumDebaters(){
		return numDebaters;
	}
	
	public ArrayList<Integer> getRandomConfig(){

		ArrayList<Integer> config = new ArrayList<Integer>(numDebaters);
		
        ArrayList<Integer> list = new ArrayList<Integer>(numDebaters);
        for(int i = 0; i < numDebaters; i++) {
            list.add(i);
        }
        
        while(list.size() > 0) {
            int index = rand.nextInt(list.size());
            config.add(list.remove(index));
        }

        return config;
	}
	
	public void printWeeklyConfig(int week){
		for (int i = 0; i < numDebaters - 1; i++) {
			System.out.print(competitors[configs.get(week).get(i)].getName() + System.lineSeparator());
		}
		
		System.out.print(competitors[configs.get(week).get(numDebaters - 1)].getName() + System.lineSeparator());
	}
	
	private float rateWeeklyConfig(int week, ArrayList<Integer> randomConfig){
		
		float rating = 0;
		
		if(0 < week && week < numWeeks){
			for (int i = 0; i < randomConfig.size(); i++) {
				int currentDebaterID = randomConfig.get(i);
				Debater currentDebater = competitors[currentDebaterID];
				rating += rateDebater(currentDebater, week, randomConfig, i);
			}
		}
		
		return rating;
	}
	
	private float rateDebater(Debater d, int week, ArrayList<Integer> randomConfig, int randomConfigPosition){
		float rating = 0;
		
		int position = getPositionFromConfiguration(randomConfigPosition);
		Debater partner = getPartnerFromConfiguration(randomConfigPosition, randomConfig);
		
		int counter = 0;
		while(week > 0){
			if(d.getTeam(week - 1) == position){
				rating += POSITION_RATING_MULTIPLIER * Math.pow(DECAY_RATE, counter);
			}
			
			if(d.getPartner(week - 1).equals(partner)){
				rating += PARTNER_RATING_MULTIPLIER * Math.pow(DECAY_RATE, counter);
			}
			
			week--;
			counter++;
		}
		
		return rating;
	}
	
	// Returns a configuration that is either below the rating threshold or has the lowest rating out of all attempts
	public ArrayList<Integer> getViableConfig(int week){
		
		ArrayList<Integer> bestConfigSoFar = null;
		double bestRatingSoFar = Double.MAX_VALUE;
		
		int counter = 0;
		double currentThreshold = INITIAL_RATING_THRESHOLD * Math.pow(THRESHOLD_GROWTH_RATE, week);
		while(bestRatingSoFar > currentThreshold && counter < MAX_NUM_CONFIGS_TO_TEST){
			
			ArrayList<Integer> randomConfig = getRandomConfig();
			double rating = rateWeeklyConfig(week, randomConfig);
			
			if(rating < bestRatingSoFar){
				bestConfigSoFar = randomConfig;
				bestRatingSoFar = rating;
			}
			
			counter++;
		}
		
		return bestConfigSoFar;
	}
	
	private Debater getPartnerFromConfiguration(int randomConfigPosition, ArrayList<Integer> randomConfig){
		Debater partner;
		
		if(randomConfigPosition % 2 == 0){
			partner = competitors[randomConfig.get(randomConfigPosition + 1)];
		} else{
			partner = competitors[randomConfig.get(randomConfigPosition - 1)];
		}
		
		return partner;
	}
	
	// Returns a week's configuration as a list of names
	public List getWeekNameList(int week){
		
		ArrayList<String> names = new ArrayList<String>();
		
		for (int i = 0; i < numDebaters; i++) {
			names.add(competitors[configs.get(week).get(i)].getName());
		}
		
		return names;
	}
	
	public ArrayList<Integer> getConfigFromNameList(ArrayList<String> names){
		ArrayList<Integer> config = new ArrayList<Integer>();
		
		while(!names.isEmpty()){
			String name = names.remove(0);
			int ID = getDebaterIDByName(name);
			if(ID == -1){
				System.out.println("Inside Semester.getConfigFromNameList: Debater " + name + " has an ID of -1");
			}
			config.add(ID);
		}
		
		return config;
	}
	
	public void printAllDebaterNames(){
		for (int i = 0; i < competitors.length; i++) {
			System.out.println("Debater " + i + ": " + competitors[i].getName() + " ID #" + competitors[i].getID());
		}
	}
	
	private int getDebaterIDByName(String name){
		int ID = -1;
		for (int i = 0; i < competitors.length; i++) {
			if(name.equals(competitors[i].getName())){
				ID = competitors[i].getID();
				break;
			}
		}
		
		return ID;
	}
	
	private int getPositionFromConfiguration(int randomConfigPosition){
		return (randomConfigPosition / 2) % 4;
	}
}
