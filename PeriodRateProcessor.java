package homework;

import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PeriodRateProcessor {
	private List<PeriodRate> lstPrData;

	public PeriodRateProcessor() {
		lstPrData = new ArrayList<>();
	}

	public List<PeriodRate> readText(String strFullFileName) throws IOException {
		FileReader fileReder = new FileReader(strFullFileName);
		BufferedReader bufReader = new BufferedReader(fileReder);
		String[] row = new String[6];
		PeriodRate tmpPeriodRate;
		
		while (bufReader.ready()) {
			// Read one row from the source text file, then split this row by
			// delimiter tab and save to a string array.
			row = bufReader.readLine().split("\\t"); 
			if(row[0].equals("\u5E63\u5225")) //skip first row 幣別...
				continue;
			
			//Create a PeriodRate object and initialize it with string array.
			tmpPeriodRate = new PeriodRate(row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2]),
					Integer.parseInt(row[3]), Double.parseDouble(row[4]), Double.parseDouble(row[5]));
			this.lstPrData.add(tmpPeriodRate);
		}
		
		fileReder.close();
		return lstPrData;
	}

	public int saveToDB(List<PeriodRate> lstPrData) throws SQLException, ClassNotFoundException {
		
		int num = 0;
	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		String connUrl = "jdbc:sqlserver://10.2.4.4:1433;databaseName=PCIS";
		Connection conn = DriverManager.getConnection(connUrl, "pcismgr", "tvpcismgr");
		Statement stmt = conn.createStatement();
		stmt.execute("truncate table PeriodRate;");
		
		String ins_stmt = "INSERT INTO PeriodRate VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(ins_stmt);
		for( PeriodRate pr : lstPrData ) {
		    pstmt.setString(1, pr.getCurrency());
		    pstmt.setInt(2, pr.getYear());
		    pstmt.setInt(3, pr.getMonth());
		    pstmt.setInt(4, pr.getPeriod());
		    pstmt.setDouble(5, pr.getBuyPrice());
		    pstmt.setDouble(6, pr.getSellPrice());
		    num += pstmt.executeUpdate();
		}
		
		return num;
	}

	public List<PeriodRate> getLstPrData() {
		return this.lstPrData;
	}

	public void setLstPrData(List<PeriodRate> lstPrData) {
		this.lstPrData = lstPrData;
	}
}
