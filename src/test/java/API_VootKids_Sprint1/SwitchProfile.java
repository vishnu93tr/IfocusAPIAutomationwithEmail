package API_VootKids_Sprint1;

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

public class SwitchProfile extends GenericMethod
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
	public void Swith_Profile() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("SwitchProfile");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
	    //started for loop
	    for(int i=1; i<=rowCount;i++)
        {
	    	
            	Row row = sh.getRow(i);
            	//fetching the cell values
            	TestType=row.getCell(0).getStringCellValue();
            	String Uid=row.getCell(2).getStringCellValue();
            	String childProfileId=row.getCell(3).getStringCellValue();
            	String deviceId=row.getCell(4).getStringCellValue();
            	String URL=row.getCell(5).getStringCellValue();
        		key2test=row.getCell(6).getStringCellValue();
        		Value2test=row.getCell(7).getStringCellValue();
        		//assign uid="" when uid is empty
        		if(Uid.equals("EMPTY"))
				{
            		Uid="";
				}
        		//calling function when uid is not passed
        		else if(Uid.equals("NOTPASS"))
				{
            		SwitchProfile.NotPassUid(childProfileId, deviceId, i, URL);
            		continue;
				}
        		//assign childprofileid="" when childprofileid is empty
        		if(childProfileId.equals("EMPTY"))
				{
        			childProfileId="";
				}
        		//calling function when childprofileId is not passed
        		if(childProfileId.equals("NOTPASS"))
				{
        			SwitchProfile.NotPassChildProfileId(Uid, deviceId, i, URL);
            		continue;
				}
        		//assign deviceId="" when it is empty
        		if(deviceId.equals("EMPTY"))
				{
        			deviceId="";
				}
        		//calling function when deviceId is not passed
        		if(deviceId.equals("NOTPASS"))
				{
        			SwitchProfile.NotPassDeviceId(Uid, childProfileId, i, URL);
            		continue;
				}
				
				//posting request
				BasicConfigurator.configure();
				Response resp1=	RestAssured.
					given().
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					queryParam("Uid",Uid).
					queryParam("childProfileId",childProfileId).
					queryParam("deviceId",deviceId).
					when().
					get(URL);
				
				resp1.prettyPrint();//printing the response
				resp1.then().assertThat().statusCode(200);//checking for statuscode=200
				
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
				else
				{
					str=resp1.then().extract().path(key2test);
					softAssert.assertEquals(Value2test,str);
				}
				
				//code to write the output and status code in excel
				FileInputStream fis1=new FileInputStream(path1);
				Workbook wb1=WorkbookFactory.create(fis1);
		
				Sheet sh1=wb1.getSheet("SwitchProfile");
				Row row1=sh1.getRow(i);
				row1.createCell(8);
				Cell cel1=row1.getCell(8, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cel1.setCellType(CellType.STRING);
				cel1.setCellValue(resp1.asString()); //writing the response back to the excel sheet
		
				Row row3=sh1.getRow(i);
				row3.createCell(9);
				Cell cel3=row3.getCell(9, MissingCellPolicy.CREATE_NULL_AS_BLANK);
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
				else if(TestType.equals("Negative"))//logic for writting pass/fail for negative scenarios
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
	    GenericMethod.write2Master(8,"SwitchProfile", 9,path1); //calling the generic method for writing back to the master sheet
	    softAssert.assertAll();
	 
	}
	//function for not passing Uid
	public static void NotPassUid(String childProfileId,String deviceId,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			queryParam("childProfileId",childProfileId).
			queryParam("deviceId",deviceId).
			when().
			get(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,8,9,"SwitchProfile",path1); 
	}
	//function for not passing childprofileId
	public static void NotPassChildProfileId(String Uid,String deviceId,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
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
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,8,9,"SwitchProfile",path1);//calling generic method to write response and status
	}
	//function for not passing deviceId
	public static void NotPassDeviceId(String Uid,String childProfileId,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			queryParam("Uid",Uid).
			queryParam("childProfileId",childProfileId).
			when().
			get(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,8,9,"SwitchProfile",path1);//calling generic method to write response and status
	}
	
}

