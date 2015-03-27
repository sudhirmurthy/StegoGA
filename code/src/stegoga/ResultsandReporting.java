package stegoga;

import java.io.FileWriter;
import java.io.IOException;

public class ResultsandReporting{

	//file writer object
	private FileWriter m_fileWriter;
	
	//CSV file header
    private static final String FILE_HEADER = "date,sourceFile,destFile,pnsr,mse";
	
	public ResultsandReporting(){
		try {
			this.m_fileWriter = new FileWriter(IConstants.REPORT_FILE);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void AppendResults(){
		//this.m_fileWriter.append(this.FILE_HEADER.toString());
	}
	
}

