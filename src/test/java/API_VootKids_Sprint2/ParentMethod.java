package API_VootKids_Sprint2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

import com.jayway.restassured.response.Response;

public class ParentMethod {
	
	static String path1="C:\\Users\\iFocus\\git\\IfocusAPIAutomationwithEmail\\VootKidsSprint2.xlsx";//Sheet path
	public static String platformname=""; //For different platform
	
	public static void writedata(int i,String Value2test, String TestType, Response resp1,String str,int celnum1,int celnum2,String sheetname) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		FileInputStream fis1=new FileInputStream(path1);
		Workbook wb1=WorkbookFactory.create(fis1);

		Sheet sh1=wb1.getSheet(sheetname);
		Row row1=sh1.getRow(i);
		row1.createCell(celnum1);
		Cell cel1=row1.getCell(celnum1, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel1.setCellType(CellType.STRING);
		cel1.setCellValue(resp1.asString());

		Row row3=sh1.getRow(i);
		row3.createCell(celnum2);
		Cell cel3=row3.getCell(celnum2, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		if(TestType.equals("Negative"))
		{	
			if(str.equals(Value2test) )
			{
				cel3.setCellValue("Pass");
			}
			else 
			{
				cel3.setCellValue("Fail");
			}
		}
		
		FileOutputStream fos=new FileOutputStream(path1);
		wb1.write(fos);

		fos.close();
	}
	public static void write2Master(int row,String sheetname,int columnum) throws EncryptedDocumentException, InvalidFormatException, IOException,NullPointerException
	{
	
		int countPass=0;
		int countFail=0;
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet(sheetname);
		//count the rows
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		for(int i=1; i<=rowCount;i++)
        {
			Row row4= sh.getRow(i);
			String status=row4.getCell(columnum).getStringCellValue();
			if(status.equals("Pass"))
			{
				countPass=countPass+1;
			}
			else
			{
				countFail=countFail+1;
			}
        }
		System.out.println(countPass);
		System.out.println(countFail);
		FileInputStream fis1=new FileInputStream(path1);
		Workbook wb1=WorkbookFactory.create(fis1);

		Sheet sh1=wb1.getSheet("Master");
		
		Row row1=sh1.getRow(row);
		row1.createCell(3);
		Cell cel1=row1.getCell(3, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel1.setCellType(CellType.NUMERIC);
		cel1.setCellValue(countPass);
		
		Row row2=sh1.getRow(row);
		row2.createCell(4);
		Cell cel2=row1.getCell(4, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel2.setCellType(CellType.NUMERIC);
		cel2.setCellValue(countFail);
		
		Row row3=sh1.getRow(row);
		row3.createCell(2);
		Cell cel3=row3.getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel3.setCellType(CellType.NUMERIC);
		cel3.setCellValue(countPass+countFail);
		
		
		
		
		FileOutputStream fos=new FileOutputStream(path1);
		wb1.write(fos);

		fos.close();
		}
	

}
