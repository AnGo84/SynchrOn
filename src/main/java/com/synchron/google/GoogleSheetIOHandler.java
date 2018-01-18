package com.synchron.google;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by AnGo on 10.04.2017.
 */
public class GoogleSheetIOHandler {
    /** Turn on the Google Sheets API:
     *  https://developers.google.com/sheets/api/quickstart/java
     *
     *  Introduction to the Google Sheets API:
     *  https://developers.google.com/sheets/api/guides/concepts
     */
    /**
     * GoogleDoc ID
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     */
    /**
     * Application name.
     */

    private static final String APPLICATION_NAME = "Google Sheets API Java";
    // RAW, USER_ENTERED, INPUT_VALUE_OPTIONS_UNSPECIFIED
    private static final String VALUE_INPUT_OPTION = "USER_ENTERED";
    private static final String ACCESS_TYPE = "offline";

    /**
     * Directory to store user credentials for this application.
     */
//    private static DataStoreDir dataStoreDir;
//    private static final java.io.File DATA_STORE_DIR = new java.io.File(
//            System.getProperty("user.home"), ".credentials/sheets.googleapis." + GOOGLE_PROJECT_NAME);
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();
    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS);
    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory dataStoreFactory;
    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    private static Sheets sheetsService = null;
    private static GoogleAPIProject googleAPIProject = null;

    //            Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE);
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize(String fileName) throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream(fileName);
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(dataStoreFactory)
                        .setAccessType(ACCESS_TYPE)
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
//        System.out.println(
//                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Sheets API client service.
     *
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    public static Sheets getSheetsService(GoogleAPIProject googleAPIProject) throws IOException {
        if (sheetsService == null || GoogleSheetIOHandler.googleAPIProject == null || !GoogleSheetIOHandler.googleAPIProject.equals(googleAPIProject)) {
//        dataStoreDir = DataStoreDir.getInstance(googleAPIProject.getFileName());
//        dataStoreFactory = new FileDataStoreFactory(dataStoreDir);
            GoogleSheetIOHandler.googleAPIProject = googleAPIProject;
            dataStoreFactory = DataStoreFactory.getInstance(DataStoreDir.getInstance(googleAPIProject.getProjectName()));
            Credential credential = authorize(googleAPIProject.getPathToJson());
            sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }
        return sheetsService;
    }

    /**
     * old method
     */
    public static List<String[]> getSheetsContent(List<List<Object>> values, SheetProperties sheetProperties) throws IOException {
        List<String[]> contentList = new ArrayList<String[]>();
//        Sheets service = getSheetsService(googleAPIProject);
        if (values == null || values.isEmpty()) {
            return contentList;
        } else {
            int rowNumber = sheetProperties.getRowFrom();
            int elementsCount = sheetProperties.getColumnsCount();
            for (List row : values) {
                String[] rowContent = new String[elementsCount + 1];
                //! НУЖНО ИЗМЕНИТЬ, ЧТОБЫ НЕ ЗАВИСИЛО ОТ colCount
                for (int i = 0; i < row.size(); i++) {
                    if (row.get(i) != null) {
                        rowContent[i] = String.valueOf((row.get(i)));
                    }
                }
                if (row.size() < elementsCount) {
                    for (int i = row.size() - 1; i < elementsCount; i++) {
                        rowContent[i] = "";
                    }
                }
                rowContent[elementsCount] = String.valueOf(rowNumber++);
                contentList.add(rowContent);
            }
            return contentList;
        }
    }

    /**
     * Create and return a list of GoogleDoc Row's values considering model range.
     *
     * @return a list of Row's values
     * @throws IOException
     */
    public static List<List<Object>> getValuesList(Sheets service, SheetProperties sheetProperties) throws IOException {
        ValueRange response = service.spreadsheets().values()
                .get(sheetProperties.getSheetId(), sheetProperties.getSheetNameWithRange())
                .execute();
        return response.getValues();
    }

    public static List<List<Object>> getValuesList(Sheets service, String sheetId, String sheetRange) throws IOException {
        ValueRange response = service.spreadsheets().values()
                .get(sheetId, sheetRange)
                .execute();
        return response.getValues();
    }

    public static List<String[]> getStringsValuesList(Sheets service, String sheetId, String sheetRange) throws IOException {
        List<List<Object>> listList = getValuesList(service, sheetId, sheetRange);
        List<String[]> stringsValues = new ArrayList<>();
        for (List<Object> value : listList) {
            stringsValues.add(value.toArray(new String[0]));
        }
        return stringsValues;
    }


    public static List<Sheet> getSpreadSheetsList(Sheets service, String spreadSheetId) throws IOException {
        // The ranges to retrieve from the spreadsheet.
        if (service == null)
            throw new NullPointerException("Argument 'Sheets' con't be NULL");
        if (spreadSheetId == null)
            throw new NullPointerException("Argument 'SpreadSheetId' con't be NULL");

        List<String> ranges = new ArrayList<>(); // TODO: Update placeholder value.

        Sheets.Spreadsheets.Get request = service.spreadsheets().get(spreadSheetId);
        // True if grid data should be returned.
        // This parameter is ignored if a field mask was set in the request.
        boolean includeGridData = false; // TODO: Update placeholder value.
        request.setRanges(ranges);

        request.setIncludeGridData(includeGridData);
        Spreadsheet response = request.execute();

        return response.getSheets();
    }

    public static UpdateValuesResponse updateSheetByRange(Sheets sheetsService, String spreadsheetId, String range, List<Object> objectList) throws IOException {
//
//     // How the input data should be interpreted.
//     String valueInputOption = ""; // TODO: Update placeholder value.
//
//     // TODO: Assign values to desired fields of `requestBody`. All existing
//     // fields will be replaced:
        ValueRange requestBody = new ValueRange();
        List<List<Object>> list = new ArrayList<>();
        list.add(objectList);
        requestBody.setValues(list);

        Sheets.Spreadsheets.Values.Update request =
                sheetsService.spreadsheets().values().update(spreadsheetId, range, requestBody);
        request.setValueInputOption(VALUE_INPUT_OPTION);

        UpdateValuesResponse response = request.execute();
//
//     // TODO: Change code below to process the `response` object:
//     System.out.println(response);
        return response;
    }


    public static void main(String[] args) throws IOException {
        String spreadSheetId = "1QgSCimAbg90mW4YJqAhr1ms0CAT1wI3pZqHPoiBqUDQ";
        String sheetName = "Sheet1";
        String sheetDataRange = "A2:H";
        String sheetColumnFrom = "A";
        String sheetColumnTill = "H";
        int sheetRowFrom = 2;
        int columnsCount = 8;
        //    private static final String GOOGLE_CLIENT_SECRETS_JSON = "/client_secret.json";
        String googleClientSecretsJson = "P:\\Java\\FX\\TableSync\\src\\main\\resources\\client_secret.json";
        String googleProjectName = "Employees";

        // Build a new authorized API client service.
//        InputStream in = GoogleSheetIOHandler.class.getResourceAsStream("/resource/client_secret.json");

        GoogleAPIProject googleAPIProject = new GoogleAPIProject();
        googleAPIProject.setProjectName(googleProjectName);
        googleAPIProject.setPathToJson(googleClientSecretsJson);

        Sheets service = getSheetsService(googleAPIProject);

        SheetProperties sheetProperties = new SheetProperties();
        sheetProperties.setSheetId(spreadSheetId);
        sheetProperties.setSheetName(sheetName);
//        sheetProperties.setDataRange(sheetDataRange);
        sheetProperties.setColumnFrom(sheetColumnFrom);
        sheetProperties.setColumnTill(sheetColumnTill);
        sheetProperties.setRowFrom(sheetRowFrom);
        sheetProperties.setColumnsCount(columnsCount);


        List<List<Object>> values = getValuesList(service, sheetProperties);
        if (values == null || values.size() == 0) {
            System.out.println("No data found.");
        } else {
            System.out.println("ID, Major");
            for (List row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
//                System.out.println(Row.size());
                System.out.printf("%s, %s\n", row.get(0), row.get(4));
            }
        }
        System.out.println("FULL: ");

        List<String[]> sheetContent = getSheetsContent(values, sheetProperties);
        for (String[] strings : sheetContent) {
            for (String string : strings) {
                System.out.print(string + " ");
            }
            System.out.println();
        }

        System.out.println("");
        System.out.println("Edit");
        List<Object> objectList = Arrays.asList((Object[]) sheetContent.get(1));
//        System.out.println(Arrays.asList(sheetContent.get(1)));
        System.out.println(objectList);
        UpdateValuesResponse response = updateSheetByRange(service, spreadSheetId, "'Form Responses 1'!A4:I", objectList);
        System.out.println(response);
    }


}
