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

import API_VootKids_Sprint1.GenericMethod;



public class Watch extends API_VootKids_Sprint1.GenericMethod {
	static String str;
	static String key2test;
	static String Value2test;
	static String TestType;	
	
	
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void watch() throws EncryptedDocumentException, InvalidFormatException, IOException {
		BasicConfigurator.configure();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path2);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("Watch");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		System.out.println("rowcount is:"+rowCount);
		 for(int i=1; i<=rowCount;i++)
	        {
			 Row row = sh.getRow(i);
             TestType=row.getCell(0).getStringCellValue();	
             String  URL=row.getCell(2).getStringCellValue();
             String   limit=row.getCell(3).getStringCellValue();
             String   offSet=row.getCell(4).getStringCellValue();
             String  ks=row.getCell(5).getStringCellValue();
             String   uId=row.getCell(6).getStringCellValue();
             String   profileId=row.getCell(7).getStringCellValue();
             key2test=row.getCell(8).getStringCellValue();
             Value2test=row.getCell(9).getStringCellValue();	
             if(limit.equals("EMPTY")) {
            	 
            	 limit="";
            	 
             }
             if(limit.equals("NOTPASS")) {
            	 Watch.NotPasslimit(i, ks, uId, profileId, URL, offSet);
            	 continue;
             }
             if(offSet.equals("EMPTY")) {
            	 
            	 offSet="";
            	 
             }
             if(offSet.equals("NOTPASS")) {
            	 
            	 Watch.NotPassoffset(i, ks, uId, profileId, URL,limit);
            	 continue;
             }	
             if(uId.equals("EMPTY")) 
             {
            	 uId="";
             }
             if(uId.equals("NOTPASS")) {
            	 
            	 Watch.NotPassuId(i, ks, offSet, profileId, URL, limit);
            	 continue;
             }
             if(ks.equals("EMPTY")) {
            	 ks="";
             }
             if(ks.equals("NOTPASS")) 
             {
            	 Watch.NotPassks(i, offSet,profileId, URL, limit, uId);
            	 continue;
             }
             if(profileId.equals("EMPTY")) 
             {
            	 profileId="";
             }
             if(profileId.equals("NOTPASS")) {
            	 Watch.NotPassprofileId(i, offSet, ks, URL, limit, uId);
            	 continue;
             }
             if(ks.equals("NOTPASS") && uId.equals("NOTPASS") && profileId.equals("NOTPASS")) {
            	 
            	 Watch.Nonmandatoryparameters(i, offSet, URL, limit);
            	 continue;
            }
             if(limit.equals("NOTPASS") && offSet.equals("NOTPASS"))
             {
            	 Watch.mandatoryparameters(i, URL, ks, uId, profileId);
            	 continue;
            	 
             }
             Response resp1=	RestAssured.
								given().
								param("limit",limit).
								param("offSet",offSet).
								param("ks",ks).
								param("uId",uId).
								param("profileId",profileId).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								when().
								get(URL);
             
		resp1.prettyPrint();
		resp1.then().assertThat().statusCode(200);
		set(resp1);
		if(TestType.equals("Positive")) 
		{	
			int mastHeadTray_size = resp1.body().path("assets[0].assets[0].items.size()");
			System.out.println(mastHeadTray_size);
			for (int j=0;j<mastHeadTray_size;j++) 
			{
				String profile=resp1.jsonPath().get("assets[0].assets[0].items["+j+"].profile");
				System.out.println(profile);
				softAssert.assertNotNull(profile);
				
				String itemType=resp1.jsonPath().get("assets[0].assets[0].items["+j+"].itemType");
				System.out.println(itemType);
				softAssert.assertNotNull(itemType);
				
				String cId=resp1.jsonPath().get("assets[0].assets[0].items["+j+"].cId");
				System.out.println(cId);
				softAssert.assertNotNull(cId);
				
				String mId=resp1.jsonPath().get("assets[0].assets[0].items["+j+"].mId");
				System.out.println(mId);
				softAssert.assertNotNull(mId);
				
				int mediaType=resp1.jsonPath().get("assets[0].assets[0].items["+j+"].mediaType");
				System.out.println(mediaType);
				softAssert.assertNotNull(mediaType);
				
				String contentType=resp1.jsonPath().get("assets[0].assets[0].items["+j+"].contentType");
				System.out.println(contentType);
				softAssert.assertNotNull(contentType);
				
				int duration=resp1.jsonPath().get("assets[0].assets[0].items["+j+"].duration");
				System.out.println(duration);
				softAssert.assertNotNull(duration);
				
				String title=resp1.jsonPath().get("assets[0].assets[0].items["+j+"].title");
				System.out.println(title);
				softAssert.assertNotNull(title);
				
				String desc=resp1.jsonPath().get("assets[0].assets[0].items["+j+"].desc");
				System.out.println(desc);
				softAssert.assertNotNull(desc);
				
				String imgURL=resp1.jsonPath().get("assets[0].assets[0].items["+j+"].imgURL");
				System.out.println(imgURL);
				softAssert.assertNotNull(imgURL);
			}
		
			int segmentedTabs_size = resp1.body().path("assets[1].segmentedTabs.size()");
			System.out.println(segmentedTabs_size);
			for(int k=0;k<segmentedTabs_size;k++) {
				
				String tabId=resp1.jsonPath().get("assets[1].segmentedTabs["+k+"].tabId");
				System.out.println(tabId);
				softAssert.assertNotNull(tabId);
				
				String tabLayout=resp1.jsonPath().get("assets[1].segmentedTabs["+k+"].tabLayout");
				System.out.println(tabLayout);
				softAssert.assertNotNull(tabLayout);
				
				String tabLabel=resp1.jsonPath().get("assets[1].segmentedTabs["+k+"].tabLabel");
				System.out.println(tabLabel);
				softAssert.assertNotNull(tabLabel);
				
				String tabContentType=resp1.jsonPath().get("assets[1].segmentedTabs["+k+"].tabContentType");
				System.out.println(tabContentType);
				softAssert.assertNotNull(tabContentType);
				
				String nextPageAPI=resp1.jsonPath().get("assets[1].segmentedTabs["+k+"].nextPageAPI");
				System.out.println(nextPageAPI);
				softAssert.assertNotNull(nextPageAPI);
			}
			int gridtray_size = resp1.body().path("assets[2].assets[0].items.size()");
			System.out.println(gridtray_size);
			for(int l=0;l<gridtray_size;l++) {
				String profile=resp1.jsonPath().get("assets[2].assets[0].items["+l+"].profile");
				System.out.println(profile);
				softAssert.assertNotNull(profile);
				
				String mId=resp1.jsonPath().get("assets[2].assets[0].items["+l+"].mId");
				System.out.println(mId);
				softAssert.assertNotNull(mId);
				
				int mediaType=resp1.jsonPath().get("assets[2].assets[0].items["+l+"].mediaType");
				System.out.println(mediaType);
				softAssert.assertNotNull(mediaType);
				
				String contentType=resp1.jsonPath().get("assets[2].assets[0].items["+l+"].contentType");
				System.out.println(contentType);
				softAssert.assertNotNull(contentType);
				
				int duration=resp1.jsonPath().get("assets[2].assets[0].items["+l+"].duration");
				System.out.println(duration);
				softAssert.assertNotNull(duration);
				
				String title=resp1.jsonPath().get("assets[2].assets[0].items["+l+"].title");
				System.out.println(title);
				softAssert.assertNotNull(title);
				
				String desc=resp1.jsonPath().get("assets[2].assets[0].items["+l+"].desc");
				System.out.println(desc);
				softAssert.assertNotNull(desc);
				
				String imgURL=resp1.jsonPath().get("assets[2].assets[0].items["+l+"].imgURL");
				System.out.println(imgURL);
				softAssert.assertNotNull(imgURL);
				
			}
			int gridtray_size1 = resp1.body().path("assets[3].assets[0].items.size()");
			System.out.println(gridtray_size1);	
			for(int l=0;l<gridtray_size1;l++) {
				String profile=resp1.jsonPath().get("assets[3].assets[0].items["+l+"].profile");
				System.out.println(profile);
				softAssert.assertNotNull(profile);
				
				String mId=resp1.jsonPath().get("assets[3].assets[0].items["+l+"].mId");
				System.out.println(mId);
				softAssert.assertNotNull(mId);
				
				int mediaType=resp1.jsonPath().get("assets[3].assets[0].items["+l+"].mediaType");
				System.out.println(mediaType);
				softAssert.assertNotNull(mediaType);
				
				String contentType=resp1.jsonPath().get("assets[3].assets[0].items["+l+"].contentType");
				System.out.println(contentType);
				softAssert.assertNotNull(contentType);
				
				int duration=resp1.jsonPath().get("assets[3].assets[0].items["+l+"].duration");
				System.out.println(duration);
				softAssert.assertNotNull(duration);
				
				String title=resp1.jsonPath().get("assets[3].assets[0].items["+l+"].title");
				System.out.println(title);
				softAssert.assertNotNull(title);
				
				String desc=resp1.jsonPath().get("assets[3].assets[0].items["+l+"].desc");
				System.out.println(desc);
				softAssert.assertNotNull(desc);
				
				String imgURL=resp1.jsonPath().get("assets[3].assets[0].items["+l+"].imgURL");
				System.out.println(imgURL);
				softAssert.assertNotNull(imgURL);
				
			}
			
			String trayContentType=resp1.jsonPath().get("assets[4].trayContentType");
			System.out.println(trayContentType);
			softAssert.assertNotNull(trayContentType);
			
			String trayLayout=resp1.jsonPath().get("assets[4].trayLayout");
			System.out.println(trayLayout);
			softAssert.assertNotNull(trayLayout);
			
			String trayName=resp1.jsonPath().get("assets[4].trayName");
			System.out.println(trayName);
			softAssert.assertNotNull(trayName);
			
			String title=resp1.jsonPath().get("assets[4].title");
			System.out.println(title);
			softAssert.assertNotNull(title);
			
			String nextPageAPI=resp1.jsonPath().get("assets[4].nextPageAPI");
			System.out.println(nextPageAPI);
			softAssert.assertNotNull(nextPageAPI);
			
			int isKidsCharacters=resp1.jsonPath().get("assets[4].isKidsCharacters");
			System.out.println(isKidsCharacters);
			softAssert.assertNotNull(isKidsCharacters);
			
			str= resp1.jsonPath().get(key2test);
			softAssert.assertEquals(Value2test,str);
		}
	        
		else
		{
			str= resp1.jsonPath().get(key2test);
			softAssert.assertEquals(Value2test,str);
		}
			FileInputStream fis1=new FileInputStream(path2);
			Workbook wb1=WorkbookFactory.create(fis1);

			Sheet sh1=wb1.getSheet("Watch");
			Row row1=sh1.getRow(i);
			row1.createCell(10);
			Cell cel1=row1.getCell(10, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cel1.setCellType(CellType.STRING);
			cel1.setCellValue(resp1.asString());

			Row row3=sh1.getRow(i);
			row3.createCell(11);
			Cell cel3=row3.getCell(11, MissingCellPolicy.CREATE_NULL_AS_BLANK);
	
			if(str.equals(Value2test))
			{
				cel3.setCellValue("Pass");
			}
			
			else 
			{
				cel3.setCellValue("Fail");
			}
			
			
        
		FileOutputStream fos=new FileOutputStream(path2);
		wb1.write(fos);
		fos.close();
		}
		 GenericMethod.write2Master(3, "Watch", 11,path2);
		softAssert.assertAll();
			
		}
	
	void set(Response resp1) {
		
	}
	
	public static void NotPassoffset(int i,String ks,String uId,String profileId,String URL,String limit ) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		BasicConfigurator.configure();
		 Response resp1=	RestAssured.
							given().
							parameter("limit",limit).
							parameter("ks",ks).
							parameter("uId",uId).
							parameter("profileId",profileId).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							get(URL);

		resp1.prettyPrint();
		
		str=resp1.then().extract().path(key2test);
		System.out.println("str is:"+str);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,10,11,"Watch",path2);
	}
	public static void NotPasslimit(int i,String ks,String uId,String profileId,String URL,String offset) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		BasicConfigurator.configure();
		 Response resp1=	RestAssured.
							given().
							parameter("offset",offset).
							parameter("ks",ks).
							parameter("uId",uId).
							parameter("profileId",profileId).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							get(URL);

		resp1.prettyPrint();
		
		str=resp1.then().extract().path(key2test);
		System.out.println("str is:"+str);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,10,11,"Watch",path2);
	}
	public static void NotPassuId(int i,String ks,String offset,String profileId,String URL,String limit) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		BasicConfigurator.configure();
		 Response resp1=	RestAssured.
							given().
							parameter("offset",offset).
							parameter("limit",limit).
							parameter("ks",ks).
							parameter("profileId",profileId).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							get(URL);

		resp1.prettyPrint();
		
		str=resp1.then().extract().path(key2test);
		System.out.println("str is:"+str);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,10,11,"Watch",path2);
	}
	public static void NotPassks(int i,String offSet,String profileId,String URL,String limit,String uId) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
				given().
				param("limit",limit).
				param("offSet",offSet).
				param("uId",uId).
				param("profileId",profileId).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				when().
				get(URL);

		resp1.prettyPrint();
		
		str=resp1.then().extract().path(key2test);
		System.out.println("str is:"+str);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,10,11,"Watch",path2);
	}
	public static void NotPassprofileId(int i,String offSet,String ks,String URL,String limit,String uId) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		BasicConfigurator.configure();
		  Response resp1=	RestAssured.
					given().
					param("limit",limit).
					param("offSet",offSet).
					param("ks",ks).
					param("uId",uId).
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					when().
					get(URL);

		resp1.prettyPrint();
		
		str=resp1.then().extract().path(key2test);
		System.out.println("str is:"+str);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,10,11,"Watch",path2);
	}
	public static void Nonmandatoryparameters(int i,String offSet,String URL,String limit) throws EncryptedDocumentException, InvalidFormatException, IOException
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
		
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,10,11,"Watch",path2);
	}
	public static void mandatoryparameters(int i,String URL,String ks,String uId,String profileId) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		BasicConfigurator.configure();
		
		  Response resp1=	RestAssured.
							given().
							param("ks",ks).
							param("uId",uId).
							param("profileId",profileId).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							get(URL);
		 
		resp1.prettyPrint();
		
		str=resp1.then().extract().path(key2test);
		System.out.println("str is:"+str);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,10,11,"Watch",path2);
	}
}

			
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
		
	        
		
	


