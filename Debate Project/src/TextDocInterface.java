import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class TextDocInterface {
	
	public static String[] getNames(String rootFolderPath){
		File namesFile = new File(rootFolderPath + "/NamesList.txt");
		String[] names = null;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(namesFile));
			ArrayList<String> namesList = new ArrayList<String>();
			String currentLine;
			while((currentLine = reader.readLine()) != null){
				namesList.add(currentLine);
			}
			
			names = new String[namesList.size()];
			namesList.toArray(names);
			
		} catch (FileNotFoundException e) {
			System.out.println("Error: Files missing.");
		} catch(IOException e){
			System.out.println("Error: IOException.");
		}
		
		return names;
	}
	
	public static ArrayList<Integer> getConfigFromTextFile(String rootFolderPath, int week, Semester sem){
		File namesFile = new File(rootFolderPath + "/Week " + week +".txt");
		ArrayList<String> namesList = new ArrayList<String>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(namesFile));
			String currentLine;
			
			// Read through the file line-by-line.  If a line begins with
			// a digit, (eg: 1st Government) then the following 2 lines are names
			while((currentLine = reader.readLine()) != null){
				if(currentLine.length() > 0){
					char c = currentLine.charAt(0);
					if(Character.isDigit(c)){
						namesList.add(reader.readLine().trim());
						namesList.add(reader.readLine().trim());
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error: Files missing.");
		} catch(IOException e){
			System.out.println("Error: IOException.");
		}
		
		return sem.getConfigFromNameList(namesList);
	}
	
	public static void commitWeekToTextFile(String rootFolderPath, int week, Semester sem){
		
		// If it's the 0th week, initialize a random configuration
		if(week == 0){
			sem.commitConfig(0,sem.getRandomConfig());
		}
		
		try {
			PrintWriter writer = new PrintWriter(rootFolderPath + "/Week " + week +".txt", "UTF-8");
			StringBuilder s = new StringBuilder();
			int numDebaters = sem.getNumDebaters();
			List names = sem.getWeekNameList(week);
			
			s.append("Week " + week + System.lineSeparator() + System.lineSeparator());
			
			for (int i = 0; i < 3; i++) {
				s.append("*****************" + System.lineSeparator());
				s.append("*     Room" + (i + 1) + "     *" + System.lineSeparator());
				s.append("*****************" + System.lineSeparator());
				s.append("-----------------" + System.lineSeparator());
				for(int j = 0; j < 4; j++){
					switch (j) {
					case 0:
						s.append("1st Government" + System.lineSeparator());
						break;
					case 1:
						s.append("1st Opposition" + System.lineSeparator());
						break;
					case 2:
						s.append("2nd Government" + System.lineSeparator());
						break;
					default:
						s.append("2nd Opposition" + System.lineSeparator());
						break;
					}
					s.append(names.remove(0) + System.lineSeparator());
					s.append(names.remove(0) + System.lineSeparator());
					s.append("-----------------" + System.lineSeparator());
				}
				
				s.append(System.lineSeparator());
			}
			
			writer.print(s);
			writer.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Error: Folder not found.");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void commitNextWeektoTextFile(String rootFolderPath, Semester s){
		
		int currentWeek = 0;
		
		while(new File(rootFolderPath + "/Week " + currentWeek +".txt").exists())
		{
			currentWeek++;
		}
		
		for (int i = 0; i < currentWeek; i++) {
			s.commitConfig(i, getConfigFromTextFile(rootFolderPath, i, s));
		}
		
		s.commitConfig(currentWeek, s.getViableConfig(currentWeek));
		
		commitWeekToTextFile(rootFolderPath, currentWeek, s);
	}
	
	
}
