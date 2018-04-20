package API_VootKids;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

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

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class GenericMethod 
{
	
	static String path1="C:\\Users\\ifocus.IFOCUSODC-PC47\\git\\API2\\VootKids.xls";//Sheet path
	public static String platformname=""; //For different platform
	
	public  Response SignUp() throws EncryptedDocumentException, InvalidFormatException, IOException  
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Auto generated email
		String SALTCHARS = "abcdefghijklmnopqrstuvwxyz123456789";
	    StringBuilder salt = new StringBuilder();
	    Random rnd = new Random();
	    while (salt.length() < 10) 
	    { 
	    	int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	        salt.append(SALTCHARS.charAt(index));
	    }
	    String saltStr = salt.toString();
	    String email= saltStr+"@gmail.com";
	    //Auto Generting PIN
	    int pin = (int)(Math.random()*9000)+1000;
	    RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet("SignUp");
		Row row=sh.getRow(1);
		
		String password=row.getCell(3).getStringCellValue();
		String deviceId=row.getCell(4).getStringCellValue();
		String deviceBrand=row.getCell(5).getStringCellValue();
		String URL=row.getCell(7).getStringCellValue();
		String key2test=row.getCell(8).getStringCellValue();
	
		
		BasicConfigurator.configure();
		Response resp=	RestAssured.
						given().
						relaxedHTTPSValidation().
						contentType(ContentType.JSON).
						accept(ContentType.JSON).
						queryParam("email",email).
						queryParam("password",password).
						queryParam("deviceId",deviceId).
						queryParam("deviceBrand",deviceBrand).
						queryParam("pin",pin).
						when().
						post(URL);
		
		
		resp.then().assertThat().statusCode(200);
		String act = resp.jsonPath().get(key2test);
		act.equals(email);
		
		FileInputStream fis1=new FileInputStream(path1);
		Workbook wb1=WorkbookFactory.create(fis1);

		Sheet sh1=wb1.getSheet("SignUp");
		Row row1=sh1.getRow(1);
		row1.createCell(10);
		Cell cel1=row1.getCell(10, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel1.setCellType(CellType.STRING);
		cel1.setCellValue(resp.asString());
		
		Row row2=sh1.getRow(1);
		row2.createCell(2);
		Cell cel2=row1.getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel2.setCellType(CellType.STRING);
		cel2.setCellValue(act);
		
		Row row4=sh1.getRow(1);
		row4.createCell(9);
		Cell cel4=row1.getCell(9, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel4.setCellType(CellType.STRING);
		cel4.setCellValue(act);

		Row row3=sh1.getRow(1);
		row3.createCell(11);
		Cell cel3=row3.getCell(11, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		if(act.equals(email)) {
		cel3.setCellValue("Pass");
		}
		else {
			cel3.setCellValue("Fail");
		}

		FileOutputStream fos=new FileOutputStream(path1);
		wb1.write(fos);

		fos.close();
		return resp;
		
	} 
	public String emailGenerator()
	{
		String SALTCHARS = "abcdefghijklmnopqrstuvwxyz123456789";
	    StringBuilder salt = new StringBuilder();
	    Random rnd = new Random();
	    while (salt.length() < 10) 
	    { 
	    	int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	        salt.append(SALTCHARS.charAt(index));
	    }
	    String saltStr = salt.toString();
	    String email= saltStr+"@gmail.com";
	    return email;
	}
	public static String passwordGenerator()
	{
		String SALTCHARS = "abcdefghijklmnopqrstuvwxyz123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%&";
	    StringBuilder salt = new StringBuilder();
	    Random rnd = new Random();
	    while (salt.length() < 10) 
	    { 
	    	int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	        salt.append(SALTCHARS.charAt(index));
	    }
	    String password = salt.toString();
	   
	    return password;
	}
	public static void writedata(int i,String Value2test, String TestType, Response resp1,String str,int celnum1,int celnum2,String sheetname) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		FileInputStream fis1=new FileInputStream(path1);
		Workbook wb1=WorkbookFactory.create(fis1);

		Sheet sh1=wb1.getSheet(sheetname);
		Row row1=sh1.getRow(i);
		row1.createCell(celnum1);
		Cell cel1=row1.getCell(celnum1, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel1.setCellType(CellType.STRING);
		cel1.setCellValue(resp1.asString());

		Row row3=sh1.getRow(i);
		row3.createCell(celnum2);
		Cell cel3=row3.getCell(celnum2, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		if(TestType.equals("Negative"))
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
	public static Response getProfiles()
	{	
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
						given().
						relaxedHTTPSValidation().
						contentType(ContentType.JSON).
						accept(ContentType.JSON).
						queryParam("Uid","487c0a7652b8405ea8472cb3ddb52e25").
						when().
						post("http://vkapiuat.voot.com/app/auth/v1/profiles.json");
		
		return resp1;
	}
}


