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
	static int str1;
	static int num;
	static String numberAsString;
	static String email;
	static String key2test;
	static String Value2test;
	static String TestType;
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void CreatePin1() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		GenericMethod g=new GenericMethod();
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
	    		Response resp=	g.SignUp();
            	Row row = sh.getRow(i);
            	//fetching the cell values
            	TestType=row.getCell(0).getStringCellValue();
            	email=row.getCell(2).getStringCellValue();
            	String pin=row.getCell(3).getStringCellValue();
            	String URL=row.getCell(4).getStringCellValue();
        		key2test=row.getCell(5).getStringCellValue();
        		Value2test=row.getCell(6).getStringCellValue();
        		
        		//if email is AUTO extrtact email from SignUp API
        		if(email.equals("AUTO"))
        		{
        			email=resp.then().extract().path("Email");
        		}
        		//if email is invalid 
        		else if(email.equals("INVALID"))
        		{
        			email="john14.doe33333333333333333333333@mailinator.com";
        		}
        		//if email is empty
        		else if(email.equals("EMPTY"))
				{
        			email="";
				}
        		//if email is null pass assign email="null"
        		else if(email.equals("NULL")) {
        			email="null";
        		}
        		//calling function for not passing email
        		else if(email.equals("NOTPASS"))
				{
            		CreatePin.NotPassemail(pin, i, URL);
            		continue;
				}
        		//assign pin="" when email is empty
        		if(pin.equals("EMPTY"))
				{
        			pin="";
				}
        		//calling function pin is not pass
        		if(pin.equals("NOTPASS"))
				{
            		CreatePin.NotPassPin(email, i, URL);
            		continue;
				}
        		
        		//posting request
				BasicConfigurator.configure();
				Response resp1=	RestAssured.
					given().
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					queryParam("email",email).
					queryParam("pin",pin).
					when().
					post(URL);
				
				resp1.prettyPrint(); //printing the response
				resp1.then().assertThat().statusCode(200);//checking the status code as 200
				
				if(TestType.equals("Positive"))//logic for positive scenarios
				{
					num=resp1.then().extract().path(key2test);//extracting the pin
					numberAsString = Integer.toString(num);//converting to string value from integer
					softAssert.assertEquals(Value2test,numberAsString);
				}
				else
				{
					str=resp1.then().extract().path(key2test);//extract the message 
					str1=resp1.then().extract().path("status.code");//extract the status code
					softAssert.assertEquals(Value2test,str);
				}
				
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
				if(TestType.equals("Positive"))// logic for writing pass/fail for positive scenarios
				{
					if(numberAsString.equals(Value2test))
					{
						cel3.setCellValue("Pass");
					}
					else
					{
						cel3.setCellValue("Fail");
					}
				}
				if(TestType.equals("Negative")) //logic for writing pass/fail for negative scenarios
				{	
					if(str.equals(Value2test) && str1==200)
					{
						cel3.setCellValue("Fail");
					} 
					else if(str.equals(Value2test))
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
	    softAssert.assertAll();
	    GenericMethod.write2Master(10, "CreatePin", 8);
	}
	//function for not passing email
	public static void NotPassemail(String pin,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
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
	//function for not passing pin
	public static void NotPassPin(String Uid,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			queryParam("email",email).
			when().
			post(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,7,8,"CreatePin");
	}
	
	
}
