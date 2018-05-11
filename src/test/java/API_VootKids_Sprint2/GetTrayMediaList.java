package API_VootKids_Sprint2;

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

public class GetTrayMediaList extends ParentMethod {
	
	static String str;
	static String key2test;
	static String Value2test;
	static String TestType;	
	static String limit;
	static String offSet;
	static String moduleId;
	static SoftAssert softAssert = new SoftAssert();
	
	@Test
	public void gettraymedialist() throws EncryptedDocumentException, InvalidFormatException, IOException {
		BasicConfigurator.configure();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("gettraymedialist");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		System.out.println("rowcount is:"+rowCount);
		 for(int i=1; i<=rowCount;i++)
	        {
			 Row row = sh.getRow(i);
             TestType=row.getCell(0).getStringCellValue();	
             String  URL=row.getCell(2).getStringCellValue();
              moduleId=row.getCell(3).getStringCellValue();
              limit=row.getCell(4).getStringCellValue();
             offSet=row.getCell(5).getStringCellValue();
             key2test=row.getCell(6).getStringCellValue();
             Value2test=row.getCell(7).getStringCellValue();
             if(moduleId.equals("EMPTY")) {
            	 
            	 moduleId="";
            	 
             }
             if(moduleId.equals("NOTPASS")) 
             {
            	 GetTrayMediaList.NotPassmoduleId(i, URL);
            	 continue;
             }
             if(limit.equals("EMPTY")) 
             {
            	 limit="";
             }
             if(limit.equals("NOTPASS")) 
             {
            	 GetTrayMediaList.NotPasslimit(i, URL);
            	 continue;
             }
           if(offSet.equals("EMPTY")) 
           {
        	   offSet="";
           }
           if(offSet.equals("NOTPASS")) 
           {
        	   GetTrayMediaList.NotPassoffset(i, URL);
        	   continue;
        	   
           }
             
             Response resp1=	RestAssured.
								given().
								param("moduleId",moduleId).
								param("limit",limit).
								param("offSet",offSet).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								when().
								get(URL);
             resp1.prettyPrint();
             if(TestType.equals("Positive")) 
             {	
            	 int size = resp1.body().path("assets.items.size()");
            	 System.out.println(size);
            	 for (int j=0;j<size;j++) 
            	 {
				String profile=resp1.jsonPath().get("assets.items["+j+"].profile");
				System.out.println(profile);
				softAssert.assertNotNull(profile);
				
				String mId=resp1.jsonPath().get("assets.items["+j+"].mId");
				System.out.println(mId);
				softAssert.assertNotNull(mId);
				
				int mediaType=resp1.jsonPath().get("assets.items["+j+"].mediaType");
				System.out.println(mediaType);
				softAssert.assertNotNull(mediaType);
				
				String contentType=resp1.jsonPath().get("assets.items["+j+"].contentType");
				System.out.println(contentType);
				softAssert.assertNotNull(contentType);
				
				
				
				int duration=resp1.jsonPath().get("assets.items["+j+"].duration");
				System.out.println(duration);
				softAssert.assertNotNull(duration);
				
				String title=resp1.jsonPath().get("assets.items["+j+"].title");
				System.out.println(title);
				softAssert.assertNotNull(title);
				
				
				String desc=resp1.jsonPath().get("assets.items["+j+"].desc");
				System.out.println(desc);
				softAssert.assertNotNull(desc);
				
				String imgURL=resp1.jsonPath().get("assets.items["+j+"].imgURL");
				System.out.println(imgURL);
				softAssert.assertNotNull(imgURL);
			
			str= resp1.jsonPath().get(key2test);
			softAssert.assertEquals(Value2test,str);		
		}		
	}
             else {
 	 
            	 str= resp1.jsonPath().get(key2test);
            	 softAssert.assertEquals(Value2test,str);	
			
			}
             FileInputStream fis1=new FileInputStream(path1);
             Workbook wb1=WorkbookFactory.create(fis1);

		Sheet sh1=wb1.getSheet("gettraymedialist");
		Row row1=sh1.getRow(i);
		row1.createCell(8);
		Cell cel1=row1.getCell(8, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel1.setCellType(CellType.STRING);
		cel1.setCellValue(resp1.asString());

		Row row3=sh1.getRow(i);
		row3.createCell(9);
		Cell cel3=row3.getCell(9, MissingCellPolicy.CREATE_NULL_AS_BLANK);


		if(str.equals(Value2test))
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
		// ParentMethod.write2Master(5, "gettraymedialist", 9);
		 softAssert.assertAll();

}
	public static void NotPasslimit(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		BasicConfigurator.configure();
		 Response resp1=	RestAssured.
							given().
							param("moduleId",moduleId).
							param("offSet",offSet).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							get(URL);

		resp1.prettyPrint();
		
		str=resp1.then().extract().path(key2test);
		System.out.println("str is:"+str);
		softAssert.assertEquals(Value2test,str);
		
		ParentMethod.writedata(i, Value2test,TestType, resp1,str,8,9,"gettraymedialist");
	}
	public static void NotPassoffset(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		BasicConfigurator.configure();
		 Response resp1=	RestAssured.
							given().
							param("moduleId",moduleId).
							param("limit",limit).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							get(URL);

		resp1.prettyPrint();
		
		str=resp1.then().extract().path(key2test);
		System.out.println("str is:"+str);
		softAssert.assertEquals(Value2test,str);
		
		ParentMethod.writedata(i, Value2test,TestType, resp1,str,8,9,"gettraymedialist");
	}
	public static void NotPassmoduleId(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		BasicConfigurator.configure();
		 Response resp1=	RestAssured.
							given().
							param("limit",limit).
							param("offSet",offSet).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							get(URL);

		resp1.prettyPrint();
		
		str=resp1.then().extract().path(key2test);
		System.out.println("str is:"+str);
		softAssert.assertEquals(Value2test,str);
		
		ParentMethod.writedata(i, Value2test,TestType, resp1,str,8,9,"gettraymedialist");
	}
	
	
}
