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
import org.testng.annotations.Test;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class Tabs extends GenericMethod
{
	@Test
	public void tabs() throws IOException, EncryptedDocumentException, InvalidFormatException 
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet("Tabs");
		Row row=sh.getRow(1);
		
		String Url=row.getCell(2).getStringCellValue();
		
		BasicConfigurator.configure();
		Response resp=	RestAssured.
						given().
						relaxedHTTPSValidation().
						contentType(ContentType.JSON).
						accept(ContentType.JSON).
						when().
						get(Url);
		resp.then().assertThat().statusCode(200);
		
		FileInputStream fis1=new FileInputStream(path1);
		Workbook wb1=WorkbookFactory.create(fis1);

		Sheet sh1=wb1.getSheet("Tabs");
		Row row1=sh1.getRow(1);
		row1.createCell(3);
		Cell cel1=row1.getCell(3, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel1.setCellType(CellType.STRING);
		cel1.setCellValue(resp.asString());

		Row row3=sh1.getRow(1);
		row3.createCell(4);
		Cell cel3=row3.getCell(4, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel3.setCellValue("Pass");
		
		FileOutputStream fos=new FileOutputStream(path1);
		wb1.write(fos);

		fos.close();
	}
}

