package API_VootKids;

import static org.hamcrest.Matchers.is;

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
import org.hamcrest.core.IsNull;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class UpdateProfile extends GenericMethod{
	static String str;
	static String key2test;
	static String Value2test;
	static String TestType;
	static String Uid;
	static String childProfileId;
	static String dob;
	static String name;
	static String icon;
	static String color;
	static String pin;
	static String Url;
	static int StatusCode;
	
	static SoftAssert softAssert = new SoftAssert();
		@Test
		public void SignUp_Kids() throws EncryptedDocumentException, InvalidFormatException, IOException
		{
			
			RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
			//Reading the excel sheet
			FileInputStream fis=new FileInputStream(path1);
			Workbook wb=WorkbookFactory.create(fis);
			//Excel sheet name Create
			Sheet sh=wb.getSheet("UpdateProfile");
			//count the no. of rows ignoring the 1st row
			int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
			
	       //started for loop
			for(int i=1; i<=rowCount;i++)
	        {
		    
		    	Row row = sh.getRow(i);
            	//fetching the cell values
		    	TestType=row.getCell(0).getStringCellValue();
		    	Uid=row.getCell(2).getStringCellValue();
		    	if(Uid.equals("EMPTY")) 
		    	{
		    		Uid="";
		    	}
		    	childProfileId=row.getCell(3).getStringCellValue();
		    	if(childProfileId.equals("EMPTY")) 
		    	{
		    		childProfileId="";
		    	}
		    	name=row.getCell(4).getStringCellValue();
		    	if(name.equals("EMPTY")) 
		    	{
		    		name="";
		    	}
		    	dob=row.getCell(5).getStringCellValue();
		    	icon=row.getCell(6).getStringCellValue();
		    	if(icon.equals("EMPTY")) 
		    	{
		    		icon="";
		    	}
		    	color=row.getCell(7).getStringCellValue();
		    	if(color.equals("EMPTY")) 
		    	{
		    		color="";
		    	}
		    	pin=row.getCell(8).getStringCellValue();
		    	
		    	Url=row.getCell(9).getStringCellValue();
		    	key2test=row.getCell(10).getStringCellValue();
		    	Value2test=row.getCell(11).getStringCellValue();
		    	if(pin.equals("EMPTY")) 
		    	{
		    		UpdateProfile.PinIsEmpty(i);
		    		continue;
		    	}
				
				buddy buddy=new buddy();
				buddy.setIcon(icon);
				buddy.setColor(color);
				
				profile profile=new profile();
				profile.setName(name);
				if(dob.equals("NA"))
				{
					profile.setDob("1992-02-14");
				}
				if(dob.equals("ALPHACHAR"))
				{
					profile.setDob("gha324");
				}
				if(dob.equals("INVALID"))
				{
					profile.setDob("19992-2-234");
				}
				if(dob.equals("EMPTY"))
				{
					profile.setDob("");
				}
				if(dob.equals("NULL"))
				{
					profile.setDob("NULL");
				}
				if(dob.equals("SPECIALCHAR"))
				{
					profile.setDob("@@@");
				}
				
				profile.setPin(pin);
				profile.setBuddy(buddy);
		
				request request=new request();
				request.setChildProfileId(childProfileId);
				request.setProfile(profile);
				
				
				//Calling function for elemnets not pass
				if(Uid.equals("NOTPASS"))
				{
					UpdateProfile.UidNotPassed(i);
					continue;
				}
				
				if(childProfileId.equals("NOTPASS"))
				{
					UpdateProfile.notPassChildProfileId(i);
					continue;
				}
				if(Uid.equals("NOTPASS"))
				{
					UpdateProfile.UidNotPassed(i);
					continue;
				}
				if(name.equals("NOTPASS"))
				{
					UpdateProfile.NameNotPassed(i);
					continue;
				}
				if(dob.equals("NOTPASS"))
				{
					UpdateProfile.DOBNotPassed(i);
					continue;
				}
				if(icon.equals("NOTPASS"))
				{
					UpdateProfile.IconNotPassed(i);
					continue;
				}
				if(color.equals("NOTPASS"))
				{
					UpdateProfile.ColorNotPassed(i);
					continue;
				}
				
				Response resp1=	RestAssured.
								given().
								body(request).
								queryParam("Uid", Uid).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								when().
								post(Url);
				
				
				
				resp1.prettyPrint();
				resp1.then().assertThat().statusCode(200);
				
				if(TestType.equals("Positive"))
				{
					str=resp1.then().extract().path(key2test);
					softAssert.assertEquals(Value2test,str);
				}
				else if(TestType.equals("Negative"))
				{
					StatusCode=resp1.then().extract().path("status.code");
					str=resp1.then().extract().path(key2test);
					softAssert.assertEquals(Value2test,str);
				}
				
				
				
				//writing into the excel sheet
				FileInputStream fis1=new FileInputStream(path1);
				Workbook wb1=WorkbookFactory.create(fis1);
				
				Sheet sh1=wb1.getSheet("UpdateProfile");
				Row row1=sh1.getRow(i);
				row1.createCell(12);
				Cell cel1=row1.getCell(12, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cel1.setCellType(CellType.STRING);
				cel1.setCellValue(resp1.asString());
		
				Row row3=sh1.getRow(i);
				row3.createCell(13);
				Cell cel3=row3.getCell(13, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				if(TestType.equals("Negative"))
				{
					if(str.equals(Value2test))
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
	        }
		public static void IconNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
		{
					RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
					buddy buddy1=new buddy();
					
					buddy1.setColor(color);
					
					profile profile1=new profile();
					profile1.setName(name);
					profile1.setPin(pin);
					profile1.setDob("1992-02-14");
					profile1.setBuddy(buddy1);
					
					request request1=new request();
					request1.setChildProfileId(childProfileId);
					request1.setProfile(profile1);
					
					Response resp1=	RestAssured.
									given().
									body(request1).
									queryParam("Uid", Uid).
									relaxedHTTPSValidation().
									contentType(ContentType.JSON).
									accept(ContentType.JSON).
									when().
									post(Url);
					
					str=resp1.then().extract().path(key2test);
					softAssert.assertEquals(Value2test,str);
					
					GenericMethod.writedata(i, Value2test, TestType, resp1, str, 12, 13, "UpdateProfile");			
					
					
					
				}
		public static void UidNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
		{
					RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
					buddy buddy1=new buddy();
					buddy1.setIcon(icon);
					buddy1.setColor(color);
					
					profile profile1=new profile();
					profile1.setName(name);
					profile1.setPin(pin);
					profile1.setDob("1992-02-14");
					profile1.setBuddy(buddy1);
					
					request request1=new request();
					request1.setChildProfileId(childProfileId);
					request1.setProfile(profile1);
					
					Response resp1=	RestAssured.
									given().
									body(request1).
									relaxedHTTPSValidation().
									contentType(ContentType.JSON).
									accept(ContentType.JSON).
									when().
									post(Url);
					
					str=resp1.then().extract().path(key2test);
					softAssert.assertEquals(Value2test,str);
					
					GenericMethod.writedata(i, Value2test, TestType, resp1, str, 12, 13, "UpdateProfile");			
					
					
					
				}
		public static void notPassChildProfileId(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
		{
					RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
					buddy buddy1=new buddy();
					buddy1.setIcon(icon);
					buddy1.setColor(color);
					
					profile profile1=new profile();
					profile1.setName(name);
					profile1.setPin(pin);
					profile1.setDob("1992-02-14");
					profile1.setBuddy(buddy1);
					
					request request1=new request();
					request1.setProfile(profile1);
					
					Response resp1=	RestAssured.
									given().
									queryParam("Uid",Uid).
									body(request1).
									relaxedHTTPSValidation().
									contentType(ContentType.JSON).
									accept(ContentType.JSON).
									when().
									post(Url);
					
					str=resp1.then().extract().path(key2test);
					softAssert.assertEquals(Value2test,str);
					
					GenericMethod.writedata(i, Value2test, TestType, resp1, str, 12, 13, "UpdateProfile");			
					
		}
		public static void NameNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
		{
					RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
					buddy buddy1=new buddy();
					buddy1.setIcon(icon);
					buddy1.setColor(color);
					
					profile profile1=new profile();
					profile1.setPin(pin);
					profile1.setDob("1992-02-14");
					profile1.setBuddy(buddy1);
					
					request request1=new request();
					request1.setChildProfileId(childProfileId);
					request1.setProfile(profile1);
					
					Response resp1=	RestAssured.
									given().
									queryParam("Uid", Uid).
									body(request1).
									relaxedHTTPSValidation().
									contentType(ContentType.JSON).
									accept(ContentType.JSON).
									when().
									post(Url);
					
					str=resp1.then().extract().path(key2test);
					softAssert.assertEquals(Value2test,str);
					
					GenericMethod.writedata(i, Value2test, TestType, resp1, str, 12, 13, "UpdateProfile");			
					
					}
		public static void DOBNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
		{
					RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
					buddy buddy1=new buddy();
					buddy1.setIcon(icon);
					buddy1.setColor(color);
					
					profile profile1=new profile();
					profile1.setName(name);
					profile1.setPin(pin);
					profile1.setBuddy(buddy1);
					
					request request1=new request();
					request1.setChildProfileId(childProfileId);
					request1.setProfile(profile1);
					
					Response resp1=	RestAssured.
									given().
									queryParam("Uid", Uid).
									body(request1).
									relaxedHTTPSValidation().
									contentType(ContentType.JSON).
									accept(ContentType.JSON).
									when().
									post(Url);
					
					str=resp1.then().extract().path(key2test);
					softAssert.assertEquals(Value2test,str);
					
					GenericMethod.writedata(i, Value2test, TestType, resp1, str, 12, 13, "UpdateProfile");			
					
					}
		public static void ColorNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
		{
					RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
					buddy buddy1=new buddy();
					buddy1.setIcon(icon);
					
					
					profile profile1=new profile();
					profile1.setName(name);
					profile1.setPin(pin);
					profile1.setDob("1992-02-14");
					profile1.setBuddy(buddy1);
					
					request request1=new request();
					request1.setChildProfileId(childProfileId);
					request1.setProfile(profile1);
					
					Response resp1=	RestAssured.
									given().
									queryParam("Uid", Uid).
									body(request1).
									relaxedHTTPSValidation().
									contentType(ContentType.JSON).
									accept(ContentType.JSON).
									when().
									post(Url);
					
					str=resp1.then().extract().path(key2test);
					softAssert.assertEquals(Value2test,str);
					
					GenericMethod.writedata(i, Value2test, TestType, resp1, str, 12, 13, "UpdateProfile");			
					
					}
		public static void PinIsEmpty(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
		{
					RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
					buddy buddy1=new buddy();
					buddy1.setIcon(icon);
					buddy1.setColor(color);
					
					profile profile1=new profile();
					profile1.setName(name);
					profile1.setDob("1992-02-14");
					profile1.setPin("");
					profile1.setBuddy(buddy1);
					
					request request1=new request();
					request1.setChildProfileId(childProfileId);
					request1.setProfile(profile1);
					
					Response resp1=	RestAssured.
									given().
									queryParam("Uid", Uid).
									body(request1).
									relaxedHTTPSValidation().
									contentType(ContentType.JSON).
									accept(ContentType.JSON).
									when().
									post(Url);
					resp1.prettyPrint();
					
					str=resp1.then().extract().path(key2test);
					softAssert.assertEquals(Value2test,str);
					
					GenericMethod.writedata(i, Value2test, TestType, resp1, str, 12, 13, "UpdateProfile");			
					
					
					
				}
		   
		    

}

	
	

		
		
		
		
	


