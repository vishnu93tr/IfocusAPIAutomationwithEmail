package API_VootKids_Sprint1;

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

public class Login extends GenericMethod
{
	static String str;
	static String str1;
	static int flag;
	static int flag1;
	static String key2test;
	static String Value2test;
	static String TestType;
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void Login_Kids() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		SoftAssert softAssert = new SoftAssert();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("Login");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
	    //started for loop
	    for(int i=1; i<=rowCount;i++)
        {
	    	
            	Row row = sh.getRow(i);
            	//fetching the cell values
            	TestType=row.getCell(0).getStringCellValue();
            	String email=row.getCell(2).getStringCellValue();
            	String password=row.getCell(3).getStringCellValue();
            	String deviceId=row.getCell(4).getStringCellValue();
        		String deviceBrand=row.getCell(5).getStringCellValue();
        		String TestType=row.getCell(0).getStringCellValue();
        		String URL=row.getCell(6).getStringCellValue();
        		key2test=row.getCell(7).getStringCellValue();
        		Value2test=row.getCell(8).getStringCellValue();
        		//passing email as empty
        		if(email.equals("EMPTY"))
				{
					email="";
				}
        		//When not passing email
        		else if(email.equals("NOTPASS"))
				{
					Login.NotPassEmail(password, deviceId, deviceBrand, i, URL);
					continue;
				}
        		//passing password as empty
        		if(password.equals("EMPTY"))
				{
            		password="";
				}
        		//When not passing password
        		else if(password.equals("NOTPASS"))
				{
            		Login.NotPassPassword(email, deviceId, deviceBrand, i, URL);
            		continue;
				}
        		//passing deviceId as empty
        		if(deviceId.equals("EMPTY"))
				{
        			deviceId="";
				}
        		//when not passing deviceId
        		else if(deviceId.equals("NOTPASS"))
				{
        			Login.NotPassdeviceId(email, password, deviceBrand, i, URL);
        			continue;
				}
        		//passing deviceBrand as empty
        		if(deviceBrand.equals("EMPTY"))
				{
        			deviceBrand="";
				}
        		//when deviceBrand is not passed
        		else if(deviceBrand.equals("NOTPASS"))
				{
        			Login.NotPassdeviceBrand(email, password, deviceId, i, URL);
        			continue;
				}
				
				//posting the request
				BasicConfigurator.configure();
				Response resp1=	RestAssured.
					given().
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					queryParam("email",email).
					queryParam("password",password).
					queryParam("deviceId",deviceId).
					queryParam("deviceBrand",deviceBrand).
					when().
					post(URL);
				
				resp1.prettyPrint(); //printing the response
				resp1.then().assertThat().statusCode(200); //checking for status code=200 in response
				
				if(TestType.equals("Positive"))//logic to test for positive TC
				{
					flag=1;//assigning flag=1 for not getting any garbage value
					String[] Keys = key2test.split(",");//split function for separating the keys to test
					for (int j=0; j < Keys.length; j++)
					{
						str=String.valueOf(resp1.then().extract().path(Keys[j]));//extracting the key value
						if(str.equals("null"))//checking the key value is null or not
						{
							flag=0;//assigning to 0 for failing the TC
						}
						System.out.println(str+"and the value of flag is: "+flag);
					}
				}

				else //logic for negative TC
				{
					str=resp1.then().extract().path(key2test);
					softAssert.assertEquals(Value2test,str);
				}
				
				//code to write the output and status code in excel
				FileInputStream fis1=new FileInputStream(path1);
				Workbook wb1=WorkbookFactory.create(fis1);
		
				Sheet sh1=wb1.getSheet("Login");
				Row row1=sh1.getRow(i);
				row1.createCell(9);
				Cell cel1=row1.getCell(9, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cel1.setCellType(CellType.STRING);
				cel1.setCellValue(resp1.asString()); //writing the response back to the excel
		
				Row row3=sh1.getRow(i);
				row3.createCell(10);
				Cell cel3=row3.getCell(10, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				if(TestType.equals("Positive")) //logic to write pass/fail for positive TC
				{
					if(flag==0)
					{
						cel3.setCellValue("Fail");
					}
					else 
					{
						cel3.setCellValue("Pass");
					}
				}
				else if(TestType.equals("Negative")) //logic for writting pass/fail in negative TC
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
	    GenericMethod.write2Master(2, "Login", 10,path1); //calling the generic method for writing back to the master sheet
	    softAssert.assertAll();
	    
 }
	//function for not passing email
	public static void NotPassEmail(String password,String deviceId,String deviceBrand,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			queryParam("password",password).
			queryParam("deviceId",deviceId).
			queryParam("deviceBrand",deviceBrand).
			
			when().
			post(URL);
		
		resp1.then().assertThat().statusCode(200);
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,9,10,"Login",path1); //calling generic method to write response and status
	}
	//function for not passing password 
	public static void NotPassPassword(String email,String deviceId,String deviceBrand,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			queryParam("email",email).
			queryParam("deviceId",deviceId).
			queryParam("deviceBrand",deviceBrand).
			
			when().
			post(URL);
		
		resp1.then().assertThat().statusCode(200);
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,9,10,"Login",path1);//calling generic method to write response and status
	}
	//function for not passing deviceId
	public static void NotPassdeviceId(String email,String password,String deviceBrand,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			queryParam("email",email).
			queryParam("password",password).
			queryParam("deviceBrand",deviceBrand).
			
			when().
			post(URL);
		
		resp1.then().assertThat().statusCode(200);
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,9,10,"Login",path1);//calling generic method to write response and status
	}
	//function for not passing deviceBrand
	public static void NotPassdeviceBrand(String email,String password,String deviceId,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			queryParam("email",email).
			queryParam("password",password).
			queryParam("deviceId",deviceId).
			
			when().
			post(URL);
		
		resp1.then().assertThat().statusCode(200);
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,9,10,"Login",path1);//calling generic method to write response and status
	}
}
