package homework;

import java.io.IOException;
import java.sql.SQLException;

public class MainClass {

	public static void main(String[] args) {
		PeriodRateProcessor objProc = new PeriodRateProcessor();
		int num=0;
		try
		{		    
			num = objProc.saveToDB(objProc.readText("gc331_current.txt"));
			System.out.println("The operation completed successfully.");
			System.out.println( num + " data rows were saved to database.");
		}
		catch(IOException ex) {			
			System.out.println(ex.toString());
		}
		catch(SQLException ex) {
			System.out.println(ex.toString());		
		}
		catch(ClassNotFoundException ex) {
			System.out.println(ex.toString());
		}
	}
}
