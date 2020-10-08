import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


public class DebateProject {

	private static String FilePath = "C:/Users/Shai/Desktop/Debate Tabs";
	private static int ThisWeek = 3;
	
	
	public static void main(String[] args) {
		
		Semester sem = new Semester(TextDocInterface.getNames(FilePath));
		
		sem.printAllDebaterNames();
		
		TextDocInterface.commitNextWeektoTextFile(FilePath, sem);
	}
}
