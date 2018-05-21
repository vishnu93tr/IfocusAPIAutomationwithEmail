package API_VootKids_Sprint1;

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

public class ForgotPassword extends GenericMethod
{
	static String str;
	static String key2test;
	static String Value2test;
	static String TestType;
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void Forgot_Password() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("ForgotPassword");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
	    //started for loop
	    for(int i=1; i<=rowCount;i++)
        {
	    	
            	Row row = sh.getRow(i);
            	//fetching the cell values
            	TestType=row.getCell(0).getStringCellValue();
            	String email=row.getCell(2).getStringCellValue();
            	String URL=row.getCell(3).getStringCellValue();
        		key2test=row.getCell(4).getStringCellValue();
        		Value2test=row.getCell(5).getStringCellValue();
        		//passing empty as email value
        		if(email.equals("EMPTY"))
				{
            		email="";
				}
        		//When email is not passed
        		if(email.equals("NOTPASS"))
				{
            		ForgotPassword.NotPassEmail(i, URL);
            		continue;
				}
				
        		//Posting Resquest to the server
        		BasicConfigurator.configure();
				Response resp1=	RestAssured.
					given().
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					queryParam("email",email).
					when().
					post(URL);
				
				resp1.prettyPrint(); //printing the response
				resp1.then().assertThat().statusCode(200); //checking for status code 
			
				str=resp1.then().extract().path(key2test);
				softAssert.assertEquals(Value2test,str);
				
				
				//code to write the output and status code in excel
				FileInputStream fis1=new FileInputStream(path1);
				Workbook wb1=WorkbookFactory.create(fis1);
		
				Sheet sh1=wb1.getSheet("ForgotPassword");
				Row row1=sh1.getRow(i);
				row1.createCell(6);
				Cell cel1=row1.getCell(6, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cel1.setCellType(CellType.STRING);
				cel1.setCellValue(resp1.asString());
		
				Row row3=sh1.getRow(i);
				row3.createCell(7);
				Cell cel3=row3.getCell(7, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				if(str.equals(Value2test) )//logic for writing pass/fail 
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
	    GenericMethod.write2Master(4,"ForgotPassword",7,path1);
	    softAssert.assertAll();
	    
	}
	//function for not passing email
	public static void NotPassEmail(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			when().
			post(URL);
		
		resp1.then().assertThat().statusCode(200);
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,6,7,"ForgotPassword",path1);
	}
}

