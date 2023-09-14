package dataDriven;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataDrivenHT {

	@SuppressWarnings("deprecation")
	public ArrayList<String> getData(String testcaseName, String sheetName) throws IOException {
		ArrayList<String> a = new ArrayList<String>();

		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir")+"\\src\\test\\resources\\testCases\\helpsToolDocUploadTC.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(fis);

		int sheets = workbook.getNumberOfSheets();
		System.out.println("no of sheets are :" + sheets);
		//System.out.println("sheet name is :" + workbook.getSheetName(0));
		for (int i = 0; i < sheets; i++) {
			if (workbook.getSheetName(i).equalsIgnoreCase(sheetName)) {
				XSSFSheet sheet = workbook.getSheetAt(i);

				Iterator<Row> rows = sheet.iterator();
				Row firstrow = rows.next();
				Iterator<Cell> ce = firstrow.cellIterator();
				int k = 0;
				int column = 0;
				while (ce.hasNext()) {
					Cell value = ce.next();

					if (value.getStringCellValue().equalsIgnoreCase("Test Steps ID")) {
						column = k;
					}

					k++;
				}
				System.out.println("sheet name is :" + workbook.getSheetName(i));
				System.out.println("column is :" + column);
				workbook.close();
				while (rows.hasNext()) {

					Row r = rows.next();

					if (CellUtil.getCell(r, column).getStringCellValue().equalsIgnoreCase(testcaseName)) {
						Iterator<Cell> cv = r.cellIterator();
						while (cv.hasNext()) {
							Cell c = cv.next();
							if (c.getCellTypeEnum() == CellType.STRING) {

								a.add(c.getStringCellValue());
							} else {

								a.add(NumberToTextConverter.toText(c.getNumericCellValue()));

							}
						}
					}

				}

			}
		}
		System.out.println("The value is" + a);
		return a;
		
		
	}

	
	public static void main(String[] args) throws IOException {
	}
	
}
