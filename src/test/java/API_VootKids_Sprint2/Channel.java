package API_VootKids_Sprint2;

import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.hamcrest.core.IsNull;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import API_VootKids.GenericMethod;


public class Channel extends GenericMethod{
	static String str;
	static String key2test;
	static String Value2test;
	static String TestType;	
	static String limit;
	static String offSet;
	static String  URL;
	static String  channelName;
	static Integer  channelId;
	static String sbu;
	static String imgURL;
	static Integer counter;
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void Channels_kidsPage() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path2);
		System.out.println(path2);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("Channel");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
		 for(int i=1; i<=rowCount;i++)
	        {
		    	
	            	Row row = sh.getRow(i);
	             TestType=row.getCell(0).getStringCellValue();	
	             limit=row.getCell(3).getStringCellValue();
	             URL=row.getCell(2).getStringCellValue();
	             offSet=row.getCell(4).getStringCellValue();
	             key2test=row.getCell(5).getStringCellValue();
	             Value2test=row.getCell(6).getStringCellValue();
	             if(limit.equals("EMPTY")) {
	            	 
	            	 limit="";
	             }
	             
	             else if(limit.equals("NOTPASS"))
	             {
	            	 Channel.NotPasslimit(i, URL);
	            	 continue;
	             }
	   
	             if(offSet.equals("EMPTY")) {
	            	 
	            	 offSet="";
	             }
	             
	             else if(offSet.equals("NOTPASS")) {
	            	 
	            	 Channel.NotPassoffSet(i, URL);
	            	 continue;
	             }
	         	           	                   
                
        //  BasicConfigurator.configure();
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
				resp1.then().assertThat().statusCode(200);//checking for status code=200
				if(TestType.equals("Positive"))
				{
					
					int sizeOfList = resp1.body().path("assets.items.size()");//taking the size of the array profiles
					System.out.println(sizeOfList);
					//logic for testing keys null or not
					String[] Keys = Value2test.split(",");
					
					for (int k=0; k < Keys.length; k++)
					{
						//logic to test dynamic keytoTest from excel and to check the keys are null or not null
						//one needs to directly update keys in excel,no need to modify code
//						counter=1;
//						for (int j=0;j<sizeOfList;j++) 
//						{
//							System.out.println(key2test+"["+j+"]."+Keys[k]);
//							channelName=String.valueOf(resp1.jsonPath().get(key2test+"["+j+"]."+Keys[k]));
//							if(channelName==null) 
//							{
//								counter=0;
//							}
//							System.out.println(channelName);
//							softAssert.assertNotNull(channelName);
//						}
					
						//logic to test key's datatypes ,if one needs to validate  for another data type ,they must modify code
						for (int j=0;j<sizeOfList;j++) 
						{
							//code to check channel name is null and asserting data type is string or not
							channelName=resp1.jsonPath().get(key2test+"["+j+"].channelName");
							softAssert.assertNotNull(channelName);
							Class<? extends Object> channelnameDatatype=resp1.jsonPath().get(key2test+"["+j+"].channelName").getClass();
							String type=channelnameDatatype.getSimpleName();
							softAssert.assertEquals(type, "String","type is not string");
							
							//code to check channelId is null and asserting data type is Integer or not
							
							channelId=resp1.jsonPath().get(key2test+"["+j+"].channelId");
							softAssert.assertNotNull(channelId);
							Class<? extends Object> channelIdDatatype=resp1.jsonPath().get(key2test+"["+j+"].channelId").getClass();
							String type1=channelIdDatatype.getSimpleName();
							softAssert.assertEquals(type1, "Integer","type is not Integer");
							
							//code to check sbu is null and asserting data type is string or not
							sbu=resp1.jsonPath().get(key2test+"["+j+"].sbu");
							softAssert.assertNotNull(sbu);
							Class<? extends Object> sbuDatatype=resp1.jsonPath().get(key2test+"["+j+"].sbu").getClass();
							String type2=sbuDatatype.getSimpleName();
							softAssert.assertEquals(type2, "String","type is not string");
							
							//code to check imgURL is null and asserting data type is string or not
							imgURL=resp1.jsonPath().get(key2test+"["+j+"].imgURL");
							softAssert.assertNotNull(imgURL);
							Class<? extends Object> imgURLdatatype=resp1.jsonPath().get(key2test+"["+j+"].imgURL").getClass();
							String type3=imgURLdatatype.getSimpleName();
							softAssert.assertEquals(type3, "String","type is not string");
						
							}
						}
					
					}
				//logic for negative scenarios
					else if(TestType.equals("Negative")) 
					{
					str= resp1.jsonPath().get(key2test);
					softAssert.assertEquals(Value2test,str);
					}
				//write logic
					FileInputStream fis1=new FileInputStream(path2);
					Workbook wb1=WorkbookFactory.create(fis1);
		
					Sheet sh1=wb1.getSheet("Channel");
					Row row1=sh1.getRow(i);
					row1.createCell(7);
					Cell cel1=row1.getCell(7, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cel1.setCellType(CellType.STRING);
					cel1.setCellValue(resp1.asString());
		
					Row row3=sh1.getRow(i);
					row3.createCell(8);
					Cell cel3=row3.getCell(8, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				//main logic to write pass fail logic based on keywords in the response	
					if(TestType.equals("Positive")) {
						if(channelName == null ||channelId == null ||sbu == null || imgURL==null ) 
						{
							cel3.setCellValue("Fail");
						}
						else 
						{
							cel3.setCellValue("Pass");
						}
						
					}
					//main logic to write pass fail based on key words in response
//					if(TestType.equals("Positive")) 
//					{
//					if(counter==0)
//					{
//					cel3.setCellValue("Fail");
//					}
//					else 
//					{
//						cel3.setCellValue("Pass");
//					}
//					}	
					
				if(TestType.equals("Negative")) {
					if(str.equals(Value2test))
					{
						cel3.setCellValue("Pass");
					}
					
					else 
					{
						cel3.setCellValue("Fail");
					}
				}	
					
	            
				FileOutputStream fos=new FileOutputStream(path2);
				wb1.write(fos);
				fos.close();
					
				}
		 
		 GenericMethod.write2Mastersprint2(2, "Channel",8);
		softAssert.assertAll();
	}
	
	
	public static void NotPasslimit(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
				given().				
				param("offSet",offSet).				
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				when().
				get(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		ParentMethod.writedata(i, Value2test,TestType, resp1,str,7,8,"Channel");
	}
	
	public static void NotPassoffSet(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
				given().
				param("limit",limit).			
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				when().
				get(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		ParentMethod.writedata(i, Value2test,TestType, resp1,str,7,8,"Channel");
	}
}


