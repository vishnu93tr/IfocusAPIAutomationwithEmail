package API_VootKids_Sprint2;

import static org.testng.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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

import API_VootKids.GenericMethod;

public class Watch_history extends ParentMethod{
	static String str;
	static String key2test;
	static String Value2test;
	static String TestType;	
	static String limit;
	static String offSet;
	static String  ks;
	static String  URL;
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void watch_history() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("Watch_history");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
		 for(int i=1; i<=rowCount;i++)
	        {
		    	
			 	Row row = sh.getRow(i);
	             TestType=row.getCell(0).getStringCellValue();	
	             limit=row.getCell(3).getStringCellValue();
	             URL=row.getCell(2).getStringCellValue();
	             if(limit.equals("EMPTY")) {
	            	 
	            	 limit=""; 
	             }
	             
	             if(limit.equals("NOTPASS")) {
	            	 
	            	 Watch_history.NotPasslimit(i, URL);
	            	 continue;
	             }
	             
	             offSet=row.getCell(4).getStringCellValue();	
	             if(offSet.equals("EMPTY")) {
	            	 
	            	 offSet="";
	             }
	             
	             if(offSet.equals("NOTPASS")) {
	            	 
	            	 Watch_history.NotPassoffset(i, URL);
	            	 continue;
	             }
	             
	              ks=row.getCell(5).getStringCellValue();	
	              if(ks.equals("EMPTY")) {
	            	  
	            	  ks="";
	              }
	              if(ks.equals("NOTPASS")) {
	            	  
	            	  Watch_history.NotPassks(i, URL);
	            	  continue;
	              }
	              
	             key2test=row.getCell(6).getStringCellValue();
	             Value2test=row.getCell(7).getStringCellValue();	
	            	
	                     
                
        //  BasicConfigurator.configure();
				Response resp1=	RestAssured.
								given().
								param("limit",limit).
								param("offSet",offSet).
								param("ks",ks).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								when().
								get(URL);
				resp1.prettyPrint();
				resp1.then().assertThat().statusCode(200);//checking for status code=200
				if(TestType.equals("Positive"))
				{
					int sizeOfList = resp1.body().path("assets.items.size()");//taking the size of the array profiles
					System.out.println(sizeOfList);
					for (int j=0;j<sizeOfList;j++) {
						
						String mId=resp1.jsonPath().get("assets.items["+j+"].mId");//checking whether the list having mId key or not
						System.out.println(mId);
						softAssert.assertNotNull(mId);
						
						int mediaType=resp1.jsonPath().get("assets.items["+j+"].mediaType");//checking whether the list having mediaType key or not
						System.out.println(mediaType);
						softAssert.assertNotNull(mediaType);
						//try {
						//String genre=resp1.jsonPath().get("assets.items["+j+"].genre");//checking whether the list having genre key or not
					//	System.out.println(genre);
						//softAssert.assertNotNull(genre);
						//}
						//catch(Exception e) {
							//System.out.println("genre at"+j+"th position is null");
							//e.printStackTrace();
					//	}
						
						String imgURL=resp1.jsonPath().get("assets.items["+j+"].imgURL");//checking whether the list having imgURL key or not
						System.out.println(imgURL);
						softAssert.assertNotNull(imgURL);
						
						int startDate=resp1.jsonPath().get("assets.items["+j+"].startDate");//checking whether the list having startDate key or not
						System.out.println(startDate);
						softAssert.assertNotNull(startDate);
						
						long endDate=resp1.jsonPath().get("assets.items["+j+"].endDate");//checking whether the list having endDate key or not
						System.out.println(endDate);
						softAssert.assertNotNull(endDate);
						
						String desc=resp1.jsonPath().get("assets.items["+j+"].desc");//checking whether the list having desc key or not
						System.out.println(desc);
						softAssert.assertNotNull(desc);
						
						String entryId=resp1.jsonPath().get("assets.items["+j+"].entryId");//checking whether the list having entryId key or not
						System.out.println(entryId);
						softAssert.assertNotNull(entryId);
						
						int season=resp1.jsonPath().get("assets.items["+j+"].season");//checking whether the list having entryId key or not
						System.out.println(season);
						softAssert.assertNotNull(season);
						
						String refSeriesTitle=resp1.jsonPath().get("assets.items["+j+"].refSeriesTitle");//checking whether the list having entryId key or not
						System.out.println(refSeriesTitle);
						softAssert.assertNotNull(refSeriesTitle);
						
						resp1.jsonPath().get("assets.items["+j+"].episodeNo").getClass();////checking whether the list having episodeNo key or not
						try
						{
							float episodeNo=resp1.jsonPath().get("assets.items["+j+"].episodeNo");
							System.out.println(episodeNo);
							softAssert.assertNotNull(episodeNo);
						}
						catch(Exception e)
						{
							int episodeNo=resp1.jsonPath().get("assets.items["+j+"].episodeNo");
							System.out.println(episodeNo);
							softAssert.assertNotNull(episodeNo);
							e.printStackTrace();
							
						}
						
						String title=resp1.jsonPath().get("assets.items["+j+"].title");//checking whether the list having title key or not
						System.out.println(title);
						softAssert.assertNotNull(title);
						
						String contentType=resp1.jsonPath().get("assets.items["+j+"].contentType");//checking whether the list having contentType key or not
						System.out.println(contentType);
						softAssert.assertNotNull(contentType);
						
						List<String> language=resp1.jsonPath().get("assets.items["+j+"].language");//checking whether the list having language key or not
						System.out.println(language);
						softAssert.assertNotNull(language);
					
						int IngestDate=resp1.jsonPath().get("assets.items["+j+"].IngestDate");//checking whether the list having IngestDate key or not
						System.out.println(IngestDate);
						softAssert.assertNotNull(IngestDate);
						
						int telecastDate=resp1.jsonPath().get("assets.items["+j+"].telecastDate");//checking whether the list having telecastDate key or not
						System.out.println("telecastDate is "+ telecastDate);
						softAssert.assertNotNull(telecastDate);
						
						String isDownable=resp1.jsonPath().get("assets.items["+j+"].isDownable");//checking whether the list having isDownable key or not
						System.out.println(isDownable);
						softAssert.assertNotNull(isDownable);
						
						int duration=resp1.jsonPath().get("assets.items["+j+"].duration");//checking whether the list having duration key or not
						System.out.println(duration);
						softAssert.assertNotNull(duration);
						
						int isThreeSixty=resp1.jsonPath().get("assets.items["+j+"].isThreeSixty");//checking whether the list having isThreeSixty key or not
						System.out.println(isThreeSixty);
						softAssert.assertNotNull(isThreeSixty);
						
						String sbu=resp1.jsonPath().get("assets.items["+j+"].sbu");//checking whether the list having sbu key or not
						System.out.println(sbu);
						softAssert.assertNotNull(sbu);
						
						int watchedDuration=resp1.jsonPath().get("assets.items["+j+"].watchedDuration");//checking whether the list having watchedDuration key or not
						System.out.println(watchedDuration);
						softAssert.assertNotNull(watchedDuration);
						
						int watchedDate=resp1.jsonPath().get("assets.items["+j+"].watchedDate");//checking whether the list having watchedDate key or not
						System.out.println(watchedDate);
						softAssert.assertNotNull(watchedDate);
						
						Boolean finishedWatching=resp1.jsonPath().get("assets.items["+j+"].finishedWatching");//checking whether the list having finishedWatching key or not
						System.out.println(finishedWatching);
						softAssert.assertNotNull(finishedWatching);
						
						str= resp1.jsonPath().get(key2test);
						softAssert.assertEquals(Value2test,str);
					}
				}
				
					else if(TestType.equals("Negative")) 
					{
					str= resp1.jsonPath().get(key2test);
					softAssert.assertEquals(Value2test,str);
					}
					FileInputStream fis1=new FileInputStream(path1);
					Workbook wb1=WorkbookFactory.create(fis1);
		
					Sheet sh1=wb1.getSheet("Watch_history");
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
		 ParentMethod.write2Master(1, "Watch_history", 9);
		 softAssert.assertAll();
	        }
	
	public static void NotPasslimit(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
				given().
				param("offSet",offSet).
				param("ks",ks).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				when().
				get(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		ParentMethod.writedata(i, Value2test,TestType, resp1,str,8,9,"Watch_history");
	}

	public static void NotPassoffset(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
				given().
				param("limit",limit).
				param("ks",ks).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				when().
				get(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		ParentMethod.writedata(i, Value2test,TestType, resp1,str,8,9,"Watch_history");
	}

	public static void NotPassks(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
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
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		ParentMethod.writedata(i, Value2test,TestType, resp1,str,8,9,"Watch_history");
	}
	
	
}

	
	
		
		
	





