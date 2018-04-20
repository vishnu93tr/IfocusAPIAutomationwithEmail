package API_VootKids;

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

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class Home extends GenericMethod 
{
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
	    	Row row=sh.getRow(1);
		
	    	String uId=row.getCell(2).getStringCellValue();
	    	if(uId.equals("EMPTY"))
	    	{
	    		uId="";
	    	}
	    	else if(uId.equals("NOTPASS"))
	    	{
	    		continue;
	    	}
	    	String profileId=row.getCell(3).getStringCellValue();
	    	if(profileId.equals("EMPTY"))
	    	{
	    		profileId="";
	    	}
	    	else if(profileId.equals("NOTPASS"))
	    	{
	    		continue;
	    	}
	    	String ks=row.getCell(4).getStringCellValue();
	    	if(ks.equals("EMPTY"))
	    	{
	    		ks="";
	    	}
	    	else if(ks.equals("NOTPASS"))
	    	{
	    		continue;
	    	}
	    	String limit=row.getCell(5).getStringCellValue();
	    	if(limit.equals("EMPTY"))
	    	{
	    		limit="";
	    	}
	    	else if(limit.equals("NOTPASS"))
	    	{
	    		limit="";
	    	}
	    	String offSet=row.getCell(6).getStringCellValue();
	    	if(offSet.equals("EMPTY"))
	    	{
	    		offSet="";
	    	}
	    	else if(offSet.equals("NOTPASS"))
	    	{
	    		offSet="";
	    	}
	    	String Url=row.getCell(7).getStringCellValue();
		
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
	    	
	    	FileInputStream fis1=new FileInputStream(path1);
			Workbook wb1=WorkbookFactory.create(fis1);

			Sheet sh1=wb1.getSheet("Home");
			Row row1=sh1.getRow(i);
			row1.createCell(8);
			Cell cel1=row1.getCell(3, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cel1.setCellType(CellType.STRING);
			cel1.setCellValue(resp.asString());

			Row row3=sh1.getRow(i);
			row3.createCell(4);
			Cell cel3=row3.getCell(4, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cel3.setCellValue("Pass");
			
			FileOutputStream fos=new FileOutputStream(path1);
			wb1.write(fos);

			fos.close();
        }
	}
}
