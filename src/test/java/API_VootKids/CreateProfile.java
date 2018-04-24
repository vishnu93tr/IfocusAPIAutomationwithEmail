package API_VootKids;

import static org.hamcrest.Matchers.hasKey;
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

public class CreateProfile extends GenericMethod{
	static String str;
	static int str1;
	static String key2test;
	static String Value2test;
	static String TestType;
	static String Uid;
	static String ks;
	static String deviceId;
	static String deviceBrand;
	static String dob;
	static String name;
	static String icon;
	static String color;
	static String pin;
	static String URL;
	static SoftAssert softAssert = new SoftAssert();
		@Test
		public void Create_Profiles() throws EncryptedDocumentException, InvalidFormatException, IOException
		{
			
			RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
			
			
			//Reading the excel sheet
			FileInputStream fis=new FileInputStream(path1);
			Workbook wb=WorkbookFactory.create(fis);
			//Excel sheet name Create
			Sheet sh=wb.getSheet("CreateProfile");
			//count the no. of rows ignoring the 1st row
			int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
			
			//started for loop
		    for(int i=1; i<=rowCount;i++)
	        {
		    	GenericMethod g=new GenericMethod();
				Response resp=	g.SignUp();
		    	Row row = sh.getRow(i);
            	//fetching the cell values
		    	TestType=row.getCell(0).getStringCellValue();
		    	Uid=row.getCell(2).getStringCellValue();
		    	key2test=row.getCell(12).getStringCellValue();
				Value2test=row.getCell(13).getStringCellValue();
		    	if(Uid.equals("AUTO"))
		    	{
		    		Uid=resp.then().extract().path("Uid");
		    	}
		    	else if(Uid.equals("EMPTY"))
		    	{
		    		Uid="";
		    	}
		    	ks=row.getCell(3).getStringCellValue();
		    	if(ks.equals("AUTO"))
		    	{
		    		ks=resp.then().extract().path("ks");
		    	}
		    	else if(ks.equals("EMPTY"))
		    	{
		    		ks="";
		    	}
		    	
		    	deviceId=row.getCell(4).getStringCellValue();
		    	if(deviceId.equals("EMPTY"))
		    	{
		    		deviceId="";
		    	}
		    	
		    	deviceBrand=row.getCell(5).getStringCellValue();
		    	if(deviceBrand.equals("EMPTY"))
		    	{
		    		deviceBrand="";
		    	}
		    	
		    	name=row.getCell(6).getStringCellValue();
		    	if(name.equals("EMPTY"))
		    	{
		    		name="";
		    	}
		    	
				dob=row.getCell(7).getStringCellValue();
				
				icon=row.getCell(8).getStringCellValue();
				if(icon.equals("EMPTY"))
		    	{
					icon="";
		    	}
				
				color=row.getCell(9).getStringCellValue();
				if(color.equals("EMPTY"))
		    	{
					color="";
		    	}
				
				pin=row.getCell(10).getStringCellValue();
				URL=row.getCell(11).getStringCellValue();
				
				String parentKS = row.getCell(3).getStringCellValue();
				
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
				if(dob.equals("null"))
				{
					profile.setDob("NULL");
				}
				if(dob.equals("SPECIALCHAR"))
				{
					profile.setDob("@@@");
				}
				
				//Calling functions for not passing elements
				if(Uid.equals("NOTPASS")) 
		    	{
		    		CreateProfile.UidNotPassed(i);
		    		continue;
		    	}
				if(ks.equals("NOTPASS")) 
		    	{
		    		CreateProfile.KSNotPassed(i);
		    		continue;
		    	}
				if(deviceId.equals("NOTPASS")) 
		    	{
		    		CreateProfile.DeviceIdNotPassed(i);
		    		continue;
		    	}
				if(deviceBrand.equals("NOTPASS"))
		    	{
		    		CreateProfile.DeviceBrandNotPassed(i);
		    		continue;
		    	}
				if(name.equals("NOTPASS"))
		    	{
		    		CreateProfile.NameNotPassed(i);
		    		continue;
		    	}
				if(dob.equals("NOTPASS")) 
				{
					CreateProfile.DOBNotPassed(i);
					continue;
					
				}
				if(icon.equals("NOTPASS"))
		    	{
					CreateProfile.IconNotPassed(i);
					continue;
		    	}
				if(color.equals("NOTPASS")) 
				{
					CreateProfile.ColorNotPassed(i);
					continue;
				}
				if(pin.equals("EMPTY")) 
				{
					CreateProfile.PinIsEmpty(i);
					continue;
				}
				
				profile.setPin(pin);
				profile.setBuddy(buddy);
		
				request request=new request();
				request.setParentKS(ks);
				request.setDeviceId(deviceId);
				request.setDeviceBrand(deviceBrand);
				request.setProfile(profile);
				
				
				
			Response resp1=	RestAssured.
								given().
								body(request).
								queryParam("Uid", Uid).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								when().
								post(URL);
				
				
				
				resp1.prettyPrint();
				resp1.then().assertThat().statusCode(200);
				
				if(TestType.equals("Positive")) 
				{
					String[] Keys = key2test.split(",");
					for (int j=0; j < Keys.length; j++)
					{
						resp1.then().body(Keys[j], is(IsNull.notNullValue()));
						
					}
//					resp1.then().body(key2test, is(IsNull.notNullValue()));
					str1=resp1.then().extract().path("status.code");
					
				}
				else if(TestType.equals("Negative"))
				{
					str=resp1.then().extract().path(key2test);
					str1=resp1.then().extract().path("status.code");
					softAssert.assertEquals(Value2test,str);
				}
				//writing into the excel sheet
				FileInputStream fis1=new FileInputStream(path1);
				Workbook wb1=WorkbookFactory.create(fis1);
				
				Sheet sh1=wb1.getSheet("CreateProfile");
				Row row1=sh1.getRow(i);
				row1.createCell(14);
				Cell cel1=row1.getCell(14, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cel1.setCellType(CellType.STRING);
				cel1.setCellValue(resp1.asString());
		
				Row row3=sh1.getRow(i);
				row3.createCell(15);
				Cell cel3=row3.getCell(15, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				
				if(TestType.equals("Negative") && str1!=200) 
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
				else if(TestType.equals("Positive") && str1==200) 
				{
					resp1.then().body(key2test, is(IsNull.notNullValue()));
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
			request1.setParentKS(ks);
			request1.setDeviceId(deviceId);
			request1.setDeviceBrand(deviceBrand);
			request1.setProfile(profile1);
			
			Response resp1=	RestAssured.
							given().
							body(request1).
							queryParam("Uid", Uid).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							post(URL);
			
			str=resp1.then().extract().path(key2test);
			softAssert.assertEquals(Value2test,str);
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile");			
			
			
			
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
			request1.setParentKS(ks);
			request1.setDeviceId(deviceId);
			request1.setDeviceBrand(deviceBrand);
			request1.setProfile(profile1);
			
			Response resp1=	RestAssured.
							given().
							body(request1).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							post(URL);
			
			str=resp1.then().extract().path(key2test);
			softAssert.assertEquals(Value2test,str);
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile");			
			
			
			
		}
public static void KSNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
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
			
			request1.setDeviceId(deviceId);
			request1.setDeviceBrand(deviceBrand);
			request1.setProfile(profile1);
			
			Response resp1=	RestAssured.
							given().
							queryParam("Uid",Uid).
							body(request1).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							post(URL);
			
			str=resp1.then().extract().path(key2test);
			softAssert.assertEquals(Value2test,str);
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile");			
			
}	
public static void DeviceIdNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
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
			request1.setParentKS(ks);
			
			request1.setDeviceBrand(deviceBrand);
			request1.setProfile(profile1);
			
			Response resp1=	RestAssured.
							given().
							queryParam("Uid",Uid).
							body(request1).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							post(URL);
			
			str=resp1.then().extract().path(key2test);
			softAssert.assertEquals(Value2test,str);
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile");			
			
			}
public static void DeviceBrandNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
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
			request1.setParentKS(ks);
			request1.setDeviceId(deviceId);
			request1.setProfile(profile1);
			
			Response resp1=	RestAssured.
							given().
							queryParam("Uid",Uid).
							body(request1).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							post(URL);
			
			str=resp1.then().extract().path(key2test);
			softAssert.assertEquals(Value2test,str);
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile");			
			
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
			request1.setParentKS(ks);
			request1.setDeviceId(deviceId);
			request1.setDeviceBrand(deviceBrand);
			request1.setProfile(profile1);
			
			Response resp1=	RestAssured.
							given().
							queryParam("Uid", Uid).
							body(request1).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							post(URL);
			
			str=resp1.then().extract().path(key2test);
			softAssert.assertEquals(Value2test,str);
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile");			
			
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
			request1.setParentKS(ks);
			request1.setDeviceId(deviceId);
			request1.setDeviceBrand(deviceBrand);
			request1.setProfile(profile1);
			
			Response resp1=	RestAssured.
							given().
							queryParam("Uid", Uid).
							body(request1).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							post(URL);
			
			str=resp1.then().extract().path(key2test);
			softAssert.assertEquals(Value2test,str);
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile");			
			
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
			request1.setParentKS(ks);
			request1.setDeviceId(deviceId);
			request1.setDeviceBrand(deviceBrand);
			request1.setProfile(profile1);
			
			Response resp1=	RestAssured.
							given().
							queryParam("Uid", Uid).
							body(request1).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							post(URL);
			
			str=resp1.then().extract().path(key2test);
			softAssert.assertEquals(Value2test,str);
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile");			
			
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
			request1.setParentKS(ks);
			request1.setDeviceId(deviceId);
			request1.setDeviceBrand(deviceBrand);
			request1.setProfile(profile1);
			
			Response resp1=	RestAssured.
							given().
							queryParam("Uid", Uid).
							body(request1).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							post(URL);
			resp1.prettyPrint();
			
			str=resp1.then().extract().path(key2test);
			softAssert.assertEquals(Value2test,str);
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile");			
			
			
			
		}



}

	
	

		
		
		
		
	


