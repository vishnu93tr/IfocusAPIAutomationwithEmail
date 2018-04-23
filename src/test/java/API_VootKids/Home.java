package API_VootKids;

import static org.testng.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class Home extends GenericMethod 
{
	static String TestType;
	static String str;
	static int str1;
	static String Value2test;
	static String Key2test;
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void home() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet("Home");
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
	    //started for loop
	    for(int i=1; i<=rowCount;i++)
        {
	    	Row row=sh.getRow(i);
	    	String offSet=row.getCell(6).getStringCellValue();
	    	String limit=row.getCell(5).getStringCellValue();
	    	String ks=row.getCell(4).getStringCellValue();
	    	String profileId=row.getCell(3).getStringCellValue();
	    	TestType=row.getCell(0).getStringCellValue();
	    	String uId=row.getCell(2).getStringCellValue();
	    	String Url=row.getCell(7).getStringCellValue();
	    	Key2test=row.getCell(8).getStringCellValue();
	    	Value2test=row.getCell(9).getStringCellValue();
	    	
	    	if(uId.equals("EMPTY"))
	    	{
	    		uId="";
	    	}
	    	else if(uId.equals("NOTPASS"))
	    	{
	    		Home.notPassUid(i, ks, profileId, limit, offSet, Url);
	    		continue;
	    	}
	    	
	    	if(profileId.equals("EMPTY"))
	    	{
	    		profileId="";
	    	}
	    	else if(profileId.equals("NOTPASS"))
	    	{
	    		Home.notPassProfileId(i, uId, ks, limit, offSet, Url);
	    		continue;
	    	}
	    	
	    	if(ks.equals("EMPTY"))
	    	{
	    		ks="";
	    	}
	    	else if(ks.equals("NOTPASS"))
	    	{
	    		Home.notPassKs(i, uId, profileId, limit, offSet, Url);
	    		continue;
	    	}
	    	
	    	if(limit.equals("EMPTY"))
	    	{
	    		limit="";
	    	}
	    	else if(limit.equals("NOTPASS"))
	    	{
	    		Home.notPassLimit(i, uId, ks, profileId, offSet, Url);
	    		continue;
	    	}
	    	
	    	if(offSet.equals("EMPTY"))
	    	{
	    		offSet="";
	    	}
	    	else if(offSet.equals("NOTPASS"))
	    	{
	    		Home.notPassOffset(i, uId, ks, profileId, limit, Url);
	    		continue;
	    	}
		
	    	BasicConfigurator.configure();
	    	Response resp=	RestAssured.
						given().
						relaxedHTTPSValidation().
						contentType(ContentType.JSON).
						accept(ContentType.JSON).
						when().
						queryParam("uId",uId).
						queryParam("profileId",profileId).
						queryParam("ks",ks).
						queryParam("limit",limit).
						queryParam("offSet",offSet).
						get(Url);
	    	resp.then().assertThat().statusCode(200);
	    
	    	if(TestType.equals("Positive"))
			{
				int sizeOfList = resp.body().path("assets.size()");
				System.out.println(sizeOfList);
				for (int j=0;j<sizeOfList;j++)
				{
					String list=resp.jsonPath().get("assets["+j+"].nextPageAPI");
					softAssert.assertNotNull(list);
					
					str=resp.then().extract().path(Key2test);
					str1=resp.then().extract().path("status.code");
					softAssert.assertEquals(Value2test,str);
				}
			}
	    	else if(TestType.equals("Negative")) 
	    	{
	    		System.out.println("LOL");
	    		str=resp.then().extract().path(Key2test);
				softAssert.assertEquals(Value2test,str);
	    	}
	    	
	    	
	    	FileInputStream fis1=new FileInputStream(path1);
			Workbook wb1=WorkbookFactory.create(fis1);

			Sheet sh1=wb1.getSheet("Home");
			Row row1=sh1.getRow(i);
			row1.createCell(10);
			Cell cel1=row1.getCell(10, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cel1.setCellType(CellType.STRING);
			cel1.setCellValue(resp.asString());

			Row row3=sh1.getRow(i);
			row3.createCell(11);
			Cell cel3=row3.getCell(11, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			if(TestType.equals("Positive") && str1==200)
			{
				cel3.setCellValue("Pass");
			}
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
	public static void notPassUid(int i,String ks,String profileId,String limit, String offset,String Url) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
    	Response resp=	RestAssured.
					given().
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					when().
					queryParam("profileId",profileId).
					queryParam("ks",ks).
					queryParam("limit",limit).
					queryParam("offSet",offset).
					get(Url);
    	resp.then().assertThat().statusCode(200);
    	
    	str=resp.then().extract().path(Key2test);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod.writedata(i, Value2test, TestType, resp, str, 10, 11, "Home");
	}
	public static void notPassKs(int i,String uId,String profileId,String limit, String offSet,String Url) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
    	Response resp=	RestAssured.
					given().
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					when().
					queryParam("profileId",profileId).
					queryParam("uId",uId).
					queryParam("limit",limit).
					queryParam("offSet",offSet).
					get(Url);
    	resp.then().assertThat().statusCode(200);
    	
    	str=resp.then().extract().path(Key2test);
		softAssert.assertEquals(Value2test,str);
    	
    	GenericMethod.writedata(i, Value2test, TestType, resp, str, 10, 11, "Home");
    	
	}
	public static void notPassProfileId(int i,String uId,String ks,String limit, String offSet,String Url) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
    	Response resp=	RestAssured.
					given().
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					when().
					queryParam("ks",ks).
					queryParam("uId",uId).
					queryParam("limit",limit).
					queryParam("offSet",offSet).
					get(Url);
    	resp.then().assertThat().statusCode(200);
    	
    	str=resp.then().extract().path(Key2test);
		softAssert.assertEquals(Value2test,str);
    	
    	GenericMethod.writedata(i, Value2test, TestType, resp, str, 10, 11, "Home");
	}
	public static void notPassLimit(int i,String uId,String ks,String profileId, String offSet,String Url) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
    	Response resp=	RestAssured.
					given().
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					when().
					queryParam("ks",ks).
					queryParam("uId",uId).
					queryParam("profileId",profileId).
					queryParam("offSet",offSet).
					get(Url);
    	resp.then().assertThat().statusCode(200);
    	
    	int sizeOfList = resp.body().path("assets.size()");
		for (int j=0;j<sizeOfList;j++)
		{
			String list=resp.jsonPath().get("assets["+j+"].nextPageAPI");
			softAssert.assertNotNull(list);
			
			str=resp.then().extract().path(Key2test);
			softAssert.assertEquals(Value2test,str);
		}
    	
    	GenericMethod.writedata(i, Value2test, TestType, resp, str, 10, 11, "Home");
	}
	public static void notPassOffset(int i,String uId,String ks,String profileId, String limit,String Url) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
    	Response resp=	RestAssured.
					given().
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					when().
					queryParam("ks",ks).
					queryParam("uId",uId).
					queryParam("profileId",profileId).
					queryParam("limit",limit).
					get(Url);
    	resp.then().assertThat().statusCode(200);
    	
    	int sizeOfList = resp.body().path("assets.size()");
		for (int j=0;j<sizeOfList;j++)
		{
			String list=resp.jsonPath().get("assets["+j+"].nextPageAPI");
			softAssert.assertNotNull(list);
			
			str=resp.then().extract().path(Key2test);
			softAssert.assertEquals(Value2test,str);
		}
    	
    	GenericMethod.writedata(i, Value2test, TestType, resp, str, 10, 11, "Home");
	}
	
}
