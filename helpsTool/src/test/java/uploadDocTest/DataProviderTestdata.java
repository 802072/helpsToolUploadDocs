package uploadDocTest;
import org.testng.annotations.DataProvider;

public class DataProviderTestdata {

	@DataProvider(name="UploadFilePath")
	public Object[][] filePath(){
		return new Object[][] {
			{"C:\\Users\\802072\\git\\helpsToolUploadDocs\\helpsTool\\src\\test\\resources\\sampleUploadDocuments\\QA Test ID 1 Sample CHA.xls","Sample 1"},
			{"C:\\Users\\802072\\git\\helpsToolUploadDocs\\helpsTool\\src\\test\\resources\\sampleUploadDocuments\\QA Test ID 2 Sample CHA.xls","Sample 2"},
			{"C:\\Users\\802072\\git\\helpsToolUploadDocs\\helpsTool\\src\\test\\resources\\sampleUploadDocuments\\QA Test ID 3 Sample CHA.xls","Sample 3"},
			{"C:\\Users\\802072\\git\\helpsToolUploadDocs\\helpsTool\\src\\test\\resources\\sampleUploadDocuments\\QA Test ID 4 Sample CHA.xls","Sample 4"},
			{"C:\\Users\\802072\\git\\helpsToolUploadDocs\\helpsTool\\src\\test\\resources\\sampleUploadDocuments\\QA Test ID 5 Sample CHA.xls","Sample 5"},
			{"C:\\Users\\802072\\git\\helpsToolUploadDocs\\helpsTool\\src\\test\\resources\\sampleUploadDocuments\\QA Test ID 6 Sample CHA.xls","Sample 6"},
			{"C:\\Users\\802072\\git\\helpsToolUploadDocs\\helpsTool\\src\\test\\resources\\sampleUploadDocuments\\QA Test ID 7 Sample CHA.xls","Sample 7"},
			{"C:\\Users\\802072\\git\\helpsToolUploadDocs\\helpsTool\\src\\test\\resources\\sampleUploadDocuments\\QA Test ID 8 Sample CHA.xls","Sample 8"},
			
		};
	}

}
