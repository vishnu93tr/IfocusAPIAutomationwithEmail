package API_VootKids_Sprint1;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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
	static Integer counter;
	static SoftAssert softAssert = new SoftAssert();
		@Test
		public void Create_Profiles() throws EncryptedDocumentException, InvalidFormatException, IOException
		{
			
			RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
			//Points to be remember-
			//1. If the cell is AUTO , this means we taking key values from running SignUp api internally
			//2. If the cell is EMPTY then the value be <key>=<""> like this.
			//3. If the cell is NA then the TC is for not mandatory parameters.
			
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
		    	else if(Uid.equals("NA"))
		    	{
		    		CreateProfile.NotMandatory(i);
		    		continue;
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
				
				
				//setting the values for icon and color
				buddy buddy=new buddy();
				buddy.setIcon(icon);
				buddy.setColor(color);
				
				//setting the values for name,dob and pin
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
				
				//setting the values for ks,deviceId and deviceBrand
				request request=new request();
				request.setParentKS(ks);
				request.setDeviceId(deviceId);
				request.setDeviceBrand(deviceBrand);
				request.setProfile(profile);
				
				
			//Posting the request	
			Response resp1=	RestAssured.
								given().
								body(request).
								queryParam("Uid", Uid).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								when().
								post(URL);
				
				
				
				resp1.prettyPrint();//print the response
				resp1.then().assertThat().statusCode(200);//checking the statuscode=200
		
				if(TestType.equals("Positive"))//logic to test for positive TC
				{
					
					//assigning flag=1 for not getting any garbage value
					String[] Keys = key2test.split(",");//split function for separating the keys to test
					for (int j=0; j < Keys.length; j++)
					{
						counter=1;
						str=resp1.then().extract().path(Keys[j]).toString();//extracting the key value
						if(str.equals("null"))//checking the key value is null or not
						{
							counter=0;//assigning to 0 for failing the TC
							softAssert.assertEquals(str,"SomeValue");
						}
						System.out.println(str+"and the value of flag is: "+counter);
						
					}
				}
				else
				{
					str=resp1.then().extract().path(key2test); //extracting the value for key to test
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
				cel1.setCellValue(resp1.asString()); //printing the response in the excel sheet
		
				Row row3=sh1.getRow(i);
				row3.createCell(15);
				Cell cel3=row3.getCell(15, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				
				if(TestType.equals("Negative"))//printing pass/fail logic for negative scenarios
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
				else if(TestType.equals("Positive"))//printing pass/fail logic for positive scenarios 
				{
					if(counter==null)
					{
						cel3.setCellValue("Fail");
					}
					else 
					{
						cel3.setCellValue("Pass");
					}
				}
				
				FileOutputStream fos=new FileOutputStream(path1);
				wb1.write(fos);
		
				fos.close();
				
	        }	
		    GenericMethod.write2Master(11, "CreateProfile", 15,path1); //calling generic method for writing in master sheet
		    softAssert.assertAll();
		    
		    
		}
		
	        
//function for not passing icon 
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
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile",path1);//calling generic method to write status and response	
			
			
			
		}
//function for passing non mandatory parameters
public static void NotMandatory(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
{
			RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
			
			Response resp1=	RestAssured.
							given().
							relaxedHTTPSValidation().
							queryParam("pin", 1223).
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							post(URL);
			
			str=resp1.then().extract().path(key2test);
			softAssert.assertEquals(Value2test,str);
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile",path1);//calling generic method to write status and response		
			
			
			
		}
//function for not passing Uid
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
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile",path1);//calling generic method to write status and response			
			
			
			
		}
//function for not passing ks token
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
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile",path1);//calling generic method to write status and response			
			
}	
//function for not passing deviceId
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
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile",path1);//calling generic method to write status and response			
			
			}
//function for not passing devicebrand 
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
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile",path1);//calling generic method to write status and response		
			
			}
//function for not passing name 
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
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile",path1);//calling generic method to write status and response			
			
			}
//function for not passing dob
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
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile",path1);//calling generic method to write status and response			
			
			}
//function for not passinf color
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
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile",path1);//calling generic method to write status and response		
			
			}
//function for passing as empty pin
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
			
			GenericMethod.writedata(i, Value2test, TestType, resp1, str, 14, 15, "CreateProfile",path1);//calling generic method to write status and response			
			
		}


}

	
	

		
		
		
		
	


