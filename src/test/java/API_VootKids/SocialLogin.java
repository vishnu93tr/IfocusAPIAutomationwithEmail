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

public class SocialLogin extends GenericMethod
{
	static String str;
	static String key2test;
	static String Value2test;
	static String TestType;
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void Social_Login() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("SocialLogin");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
	    //started for loop
	    for(int i=1; i<=rowCount;i++)
        {
	    	
            	Row row = sh.getRow(i);
            	//fetching the cell values
            	TestType=row.getCell(0).getStringCellValue();
            	String Uid=row.getCell(2).getStringCellValue();
            	String deviceId=row.getCell(3).getStringCellValue();
            	String deviceBrand=row.getCell(4).getStringCellValue();
            	String URL=row.getCell(5).getStringCellValue();
        		key2test=row.getCell(6).getStringCellValue();
        		Value2test=row.getCell(7).getStringCellValue();
        		if(Uid.equals("EMPTY")) //passing Uid as empty
				{
            		Uid="";
				}
        		else if(Uid.equals("NOTPASS")) //calling function when uid is not passed in request
				{
            		SocialLogin.NotPassUid(deviceId, i, URL, deviceBrand);
            		continue;
				}
        		if(deviceId.equals("EMPTY"))//passing deviceId as empty
				{
            		deviceId="";
				}
        		else if(deviceId.equals("NOTPASS"))//calling function when deviceId is not passed in request
				{
            		SocialLogin.NotPassdeviceId(Uid, i, URL, deviceBrand);
            		continue;
				}
        		if(deviceBrand.equals("EMPTY")) //passing empty value when deviceBrand is empty
        		{
        			deviceBrand="";
        		}
        		else if(deviceBrand.equals("NOTPASS"))//calling function for not passing devicebrand
        		{
        			SocialLogin.NotPassdevicebrand(Uid, i, URL, deviceId);
        			continue;
        		}
				
        		//Posting request to the server
        		BasicConfigurator.configure();
				Response resp1=	RestAssured.
					given().
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					queryParam("Uid",Uid).
					queryParam("deviceId",deviceId).
					queryParam("deviceBrand",deviceBrand).
					when().
					get(URL);
				
				resp1.prettyPrint(); //printing the response
				resp1.then().assertThat().statusCode(200); //checking for status code=200 in response
				
				if(TestType.equals("Positive"))//logic to test for positive TC
				{
					String[] Keys = key2test.split(",");
					for (int j=0; j < Keys.length; j++)
					{
						resp1.then().body(Keys[j], is(IsNull.notNullValue()));
						
					}
				}
				else//logic to test for negative TC
				{
					str=resp1.then().extract().path(key2test);
					softAssert.assertEquals(Value2test,str);
				}
				
				//code to write the output and status code in excel
				FileInputStream fis1=new FileInputStream(path1);
				Workbook wb1=WorkbookFactory.create(fis1);
		
				Sheet sh1=wb1.getSheet("SocialLogin");
				Row row1=sh1.getRow(i);
				row1.createCell(8);
				Cell cel1=row1.getCell(8, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cel1.setCellType(CellType.STRING);
				cel1.setCellValue(resp1.asString());
		
				Row row3=sh1.getRow(i);
				row3.createCell(9);
				Cell cel3=row3.getCell(9, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				if(TestType.equals("Positive")) //logic to write pass/fail for positive TC
				{
					String[] Keys = key2test.split(",");
					for (int j=0; j < Keys.length; j++)
					{
						resp1.then().body(Keys[j], is(IsNull.notNullValue()));
						
					}
					cel3.setCellValue("Pass");
				}
				else if(TestType.equals("Negative")) //logic to write pass/fail for negative TC
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
				else if(TestType.equals("Negative") && Value2test.equals("OK")) //logic to write pass/fail for negative TC
				{	
					cel3.setCellValue("Fail");
				}
				FileOutputStream fos=new FileOutputStream(path1);
				wb1.write(fos);
		
				fos.close();
				
		}
	    GenericMethod.write2Master(3,"SocialLogin",9);
	    softAssert.assertAll();
	    
	}
	//function for not passing Uid
	public static void NotPassUid(String deviceId,int i,String URL,String deviceBrand) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			queryParam("deviceId",deviceId).
			queryParam("deviceBrand",deviceBrand).
			when().
			get(URL);
		
		resp1.then().assertThat().statusCode(200);
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,8,9,"SocialLogin");
	}
	//function for not passing deviceId
	public static void NotPassdeviceId(String Uid,int i,String URL,String deviceBrand) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			queryParam("Uid",Uid).
			queryParam("deviceBrand",deviceBrand).
			when().
			get(URL);
		
		resp1.then().assertThat().statusCode(200);
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,8,9,"SocialLogin");
	}
	//function for not passing deviceBrand
	public static void NotPassdevicebrand(String Uid,int i,String URL,String deviceId) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			queryParam("Uid",Uid).
			queryParam("deviceId",deviceId).
			when().
			get(URL);
		
		resp1.then().assertThat().statusCode(200);
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,8,9,"SocialLogin");
	}
}
