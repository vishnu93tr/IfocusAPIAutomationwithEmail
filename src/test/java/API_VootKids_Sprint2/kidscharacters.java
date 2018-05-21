package API_VootKids_Sprint2;

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
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import API_VootKids_Sprint1.GenericMethod;



public class kidscharacters  extends API_VootKids_Sprint1.GenericMethod{
	
	static String str;
	static String key2test;
	static String Value2test;
	static String TestType;
	static Integer counter;
	static String singleVar;
	static Boolean bool;
	static SoftAssert softAssert = new SoftAssert();

	@Test
	public void kidsCharacters() throws EncryptedDocumentException, InvalidFormatException, IOException {
		
		BasicConfigurator.configure();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path2);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("kidscharacters");
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
             key2test=row.getCell(5).getStringCellValue();
             Value2test=row.getCell(6).getStringCellValue();
             if(limit.equals("EMPTY")) {
            	 limit="";
             }
             if(limit.equals("NOTPASS")) {
            	 
            	 kidscharacters.NotPasslimit(i, URL, offSet);
            	 continue;
            	 
             }
             if(offSet.equals("EMPTY")) {
            	 offSet="";
             }
             if(offSet.equals("NOTPASS")) {
            	 
            	 kidscharacters.NotPassoffset(i, URL, limit);
            	 continue;
            	 
             }
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
             if(TestType.equals("Positive")) 
     		{	
            	 ArrayList<String> expectedType1 = new ArrayList<String>();
            	 expectedType1.add("String");
            	 expectedType1.add("Integer");
            	 expectedType1.add("String");
					
            	 expectedType1.add("String");
            	 expectedType1.add("String");
            	 expectedType1.add("String");
					
            	 expectedType1.add("Integer");
            	 expectedType1.add("ArrayList");
            	 expectedType1.add("String");
					
            	 expectedType1.add("Integer");
            	 expectedType1.add("Long");
            	 expectedType1.add("String");
					
            	 expectedType1.add("String");
					
					
					ArrayList<String> myDatatype = new ArrayList<String>();
					int sizeOfList = resp1.body().path("assets.items.size()");//taking the size of the array profiles
					System.out.println(sizeOfList);
					//logic for testing keys null or not
					String[] Keys = Value2test.split(",");
					for (int k=0; k <sizeOfList ; k++)
					{
						myDatatype.removeAll(myDatatype);//using the arraylist for next time, to get next item datatype
						counter=1;
						for (int j=0;j<Keys.length;j++) 
						{
							
							singleVar=resp1.jsonPath().get(key2test+"["+k+"]."+Keys[j]).toString();
							System.out.println(singleVar+"==========");
							Class<? extends Object> channelnameDatatype=resp1.jsonPath().get(key2test+"["+k+"]."+Keys[j]).getClass();
							String type=channelnameDatatype.getSimpleName();//extracting the datatype
							myDatatype.add(type);//append the elements into arraylist
							if(singleVar.equals("null")) 
							{
								counter=0;
							}
							System.out.println(singleVar);
							softAssert.assertNotNull(singleVar);
						}
						System.out.println(myDatatype);//print the arraylist of response datatype
						System.out.println(expectedType1);
						bool=myDatatype.equals(expectedType1);
						System.out.println(bool);
						
					}
     				
     		}
             else 
             {
            	 
            	 str= resp1.jsonPath().get(key2test);
  				softAssert.assertEquals(Value2test,str);	
     				
     			}
             FileInputStream fis1=new FileInputStream(path2);
				Workbook wb1=WorkbookFactory.create(fis1);
	
				Sheet sh1=wb1.getSheet("kidscharacters");
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
					if(counter==0 || bool==false)
				
					{
						cel3.setCellValue("Fail");
					}
					else 
					{
						cel3.setCellValue("Pass");
					}
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
			
         
			FileOutputStream fos=new FileOutputStream(path2);
			wb1.write(fos);
			fos.close();
			 
	        }
		 GenericMethod.write2Master(4, "kidscharacters", 8,path2);
		 softAssert.assertAll();
	}
	public static void NotPasslimit(int i,String URL,String offSet) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		BasicConfigurator.configure();
		 Response resp1=	RestAssured.
							given().
							parameter("offSet",offSet).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							get(URL);

		resp1.prettyPrint();
		
		str=resp1.then().extract().path(key2test);
		System.out.println("str is:"+str);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,7,8,"kidscharacters",path2);
	}
	public static void NotPassoffset(int i,String URL,String limit) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		BasicConfigurator.configure();
		 Response resp1=	RestAssured.
							given().
							parameter("limit",limit).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							when().
							get(URL);

		resp1.prettyPrint();
		
		str=resp1.then().extract().path(key2test);
		System.out.println("str is:"+str);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod.writedata(i, Value2test,TestType, resp1,str,7,8,"kidscharacters",path2);
	}

}

