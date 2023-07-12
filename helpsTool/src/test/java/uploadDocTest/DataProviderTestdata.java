package uploadDocTest;
import org.testng.annotations.DataProvider;

public class DataProviderTestdata {

	@DataProvider(name="UploadFilePath")
	public Object[][] filePath(){
		return new Object[][] {
			{"C:\\Users\\802072\\eclipse-workspace1\\helpsTool\\src\\test\\resources\\testUploadDocuments\\QA Template.xlsx"},
			{"C:\\Users\\802072\\eclipse-workspace1\\helpsTool\\src\\test\\resources\\testUploadDocuments\\QA Test ID 1 Sample CHA.xls"},
			{"C:\\Users\\802072\\eclipse-workspace1\\helpsTool\\src\\test\\resources\\testUploadDocuments\\QA Test ID 2 Sample CHA.xls"},
			{"C:\\Users\\802072\\eclipse-workspace1\\helpsTool\\src\\test\\resources\\testUploadDocuments\\QA Test ID 3 Sample CHA.xls"},
			{"C:\\Users\\802072\\eclipse-workspace1\\helpsTool\\src\\test\\resources\\testUploadDocuments\\QA Test ID 4 Sample CHA.xls"},
			{"C:\\Users\\802072\\eclipse-workspace1\\helpsTool\\src\\test\\resources\\testUploadDocuments\\QA Test ID 5 Sample CHA.xls"},
			{"C:\\Users\\802072\\eclipse-workspace1\\helpsTool\\src\\test\\resources\\testUploadDocuments\\QA Test ID 6 Sample CHA.xls"},
			{"C:\\Users\\802072\\eclipse-workspace1\\helpsTool\\src\\test\\resources\\testUploadDocuments\\QA Test ID 7 Sample CHA.xls"},
			{"C:\\Users\\802072\\eclipse-workspace1\\helpsTool\\src\\test\\resources\\testUploadDocuments\\QA Test ID 8 Sample CHA.xls"},
			
		};
	}

}
