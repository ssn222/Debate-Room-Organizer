import java.util.ArrayList;


public class Debater {
	
	private int numWeeks = 12;
	
	private String name;
	public int[] teams;
	public Debater[] partners;
	public int ID = -1;
	
	public Debater(String nameInput){
		name = nameInput;
		teams = new int[numWeeks];
		partners = new Debater[numWeeks];
	}
	
	public String getName(){
		return name;
	}
	
	public void setPartner(int week, Debater partner){
		if(week >= 0 && week < partners.length){
			partners[week] = partner;
		}
	}
	
	public Debater getPartner(int week){
		return partners[week];
	}
	
	public int getTeam(int week){
		return teams[week];
	}
	
	public void setTeam(int week, int team){
		
		if(week >= 0 && week < teams.length){
			teams[week] = team;
		}
	}

	public void setID(int inputID){
		ID = inputID;
	}
	
	public int getID(){
		return ID;
	}
}
