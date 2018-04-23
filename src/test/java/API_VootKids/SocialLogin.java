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
            	String URL=row.getCell(4).getStringCellValue();
        		key2test=row.getCell(5).getStringCellValue();
        		Value2test=row.getCell(6).getStringCellValue();
        		if(Uid.equals("EMPTY"))
				{
            		Uid="";
				}
        		else if(Uid.equals("NOTPASS"))
				{
            		SocialLogin.NotPassUid(deviceId, i, URL);
            		continue;
				}
        		if(deviceId.equals("EMPTY"))
				{
            		deviceId="";
				}
        		if(deviceId.equals("NOTPASS"))
				{
            		SocialLogin.NotPassdeviceId(Uid, i, URL);
            		continue;
				}
				//when email is empty
				
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
				//printing the response
				resp1.prettyPrint();
				resp1.then().assertThat().statusCode(200);
				
				if(TestType.equals("Positive"))
				{
					String[] Keys = key2test.split(",");
					for (int j=0; j < Keys.length; j++)
					{
						resp1.then().body(Keys[j], is(IsNull.notNullValue()));
						
					}
				}
				else
				{
					str=resp1.then().extract().path(key2test);
					softAssert.assertEquals(Value2test,str);
				}
				
				//code to write the output and status code in excel
				FileInputStream fis1=new FileInputStream(path1);
				Workbook wb1=WorkbookFactory.create(fis1);
		
				Sheet sh1=wb1.getSheet("SocialLogin");
				Row row1=sh1.getRow(i);
				row1.createCell(7);
				Cell cel1=row1.getCell(7, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cel1.setCellType(CellType.STRING);
				cel1.setCellValue(resp1.asString());
		
				Row row3=sh1.getRow(i);
				row3.createCell(8);
				Cell cel3=row3.getCell(8, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				if(TestType.equals("Positive"))
				{
					String[] Keys = key2test.split(",");
					for (int j=0; j < Keys.length; j++)
					{
						resp1.then().body(Keys[j], is(IsNull.notNullValue()));
						
					}
					cel3.setCellValue("Pass");
				}
				else if(TestType.equals("Negative"))
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
				else if(TestType.equals("Negative") && Value2test.equals("OK"))
				{	
					cel3.setCellValue("Fail");
				}
				FileOutputStream fos=new FileOutputStream(path1);
				wb1.write(fos);
		
				fos.close();
				
		}
	    softAssert.assertAll();
	}
	//function for not passing Uid
	public static void NotPassUid(String deviceId,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			queryParam("deviceId",deviceId).
			when().
			get(URL);
		
		resp1.then().assertThat().statusCode(200);
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,7,8,"SocialLogin");
	}
	//function for not passing deviceId
	public static void NotPassdeviceId(String Uid,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			queryParam("Uid",Uid).
			when().
			get(URL);
		
		resp1.then().assertThat().statusCode(200);
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,7,8,"SocialLogin");
	}
}
