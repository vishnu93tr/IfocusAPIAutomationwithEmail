package API_VootKids;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class CreatePin extends GenericMethod
{
	static String str;
	static String key2test;
	static String Value2test;
	static String TestType;
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void CreatePin() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("CreatePin");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
	    //started for loop
	    for(int i=1; i<=rowCount;i++)
        {
	    	
            	Row row = sh.getRow(i);
            	//fetching the cell values
            	TestType=row.getCell(0).getStringCellValue();
            	String Uid=row.getCell(2).getStringCellValue();
            	String pin=row.getCell(3).getStringCellValue();
            	String URL=row.getCell(4).getStringCellValue();
        		key2test=row.getCell(5).getStringCellValue();
        		Value2test=row.getCell(6).getStringCellValue();
        		if(Uid.equals("EMPTY"))
				{
            		Uid="";
				}
        		else if(Uid.equals("NOTPASS"))
				{
            		CreatePin.NotPassUid(pin, i, URL);
            		continue;
				}
        		if(pin.equals("EMPTY"))
				{
        			pin="";
				}
        		if(pin.equals("NOTPASS"))
				{
            		CreatePin.NotPassPin(Uid, i, URL);
            		continue;
				}
        		
				BasicConfigurator.configure();
				Response resp1=	RestAssured.
					given().
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					queryParam("Uid",Uid).
					queryParam("pin",pin).
					when().
					post(URL);
				//printing the response
				resp1.prettyPrint();
				resp1.then().assertThat().statusCode(200);
				
				str=resp1.then().extract().path(key2test);
				softAssert.assertEquals(Value2test,str);
				
				//code to write the output and status code in excel
				FileInputStream fis1=new FileInputStream(path1);
				Workbook wb1=WorkbookFactory.create(fis1);
		
				Sheet sh1=wb1.getSheet("CreatePin");
				Row row1=sh1.getRow(i);
				row1.createCell(7);
				Cell cel1=row1.getCell(7, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cel1.setCellType(CellType.STRING);
				cel1.setCellValue(resp1.asString());
		
				Row row3=sh1.getRow(i);
				row3.createCell(8);
				Cell cel3=row3.getCell(8, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				if(str.equals(Value2test) )
				{
					cel3.setCellValue("Pass");
				}
				else 
				{
					cel3.setCellValue("Fail");
				}
				
				FileOutputStream fos=new FileOutputStream(path1);
				wb1.write(fos);
		
				fos.close();
				
		}
	    softAssert.assertAll();
	}
	public static void NotPassUid(String pin,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			queryParam("pin",pin).
			when().
			post(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,7,8,"CreatePin");
	}
	public static void NotPassPin(String Uid,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			queryParam("Uid",Uid).
			when().
			post(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,7,8,"CreatePin");
	}
	
	
}
