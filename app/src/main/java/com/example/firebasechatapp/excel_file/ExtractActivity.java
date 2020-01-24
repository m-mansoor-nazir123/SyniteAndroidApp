package com.example.firebasechatapp.excel_file;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.example.firebasechatapp.ExampleDialog;
import com.example.firebasechatapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ExtractActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {

    Spinner company;
    Button check;
    DateRangeCalendarView dateRangeCalendarView;
    ArrayList<String> array;
    List<String> categories;
    Row roww;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);

        company = findViewById(R.id.spinner);
        dateRangeCalendarView = findViewById(R.id.calendeer);


        check = findViewById(R.id.check);


        dateRangeCalendarView.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar startDate) {

            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                array = new ArrayList<String>();
                while (startDate.before(endDate)) {

                    String time = startDate.getTime().toString();
                    String month = time.substring(4, 7);
                    String date = time.substring(8, 10);
                    String year = time.substring(30, 34);
                    array.add(month + "/" + date + "/" + year);
                    startDate.add(Calendar.DAY_OF_YEAR, 1);
                }
                Toast.makeText(getApplicationContext(), "Start Date: " + startDate.getTime().toString() + " End date: " + endDate.getTime().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        spiner();


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyExistingWorkbook();
                //CheckExcelFile();
            }
        });

        openDialog();
       // createNewFile();

    }

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    private void modifyExistingWorkbook() {
        {
            String text = company.getSelectedItem().toString();
            Workbook wb = new HSSFWorkbook();
            int flag=0;

                    try {
                        FileInputStream inputStream2=new FileInputStream(new File(getExternalFilesDir(null), "extracted.xls"));
                        FileInputStream inputStream = new FileInputStream(new File(getExternalFilesDir(null), "master.xls"));
                        Workbook workbook = WorkbookFactory.create(inputStream);
                        Sheet sheet = workbook.getSheetAt(0);



                        Workbook workbook2 = WorkbookFactory.create(inputStream2);
                        Sheet sheet2 = workbook2.getSheetAt(0);
                                int count=7;
                        int lastrow =sheet.getLastRowNum();
                        int finalLastRow=lastrow+count;
                        //seting color of excel files
                        CellStyle GreenCell= workbook2.createCellStyle();
                        GreenCell.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
                        GreenCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);


                        CellStyle YellowCell= workbook2.createCellStyle();
                        YellowCell.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
                        YellowCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

                        CellStyle Red= workbook2.createCellStyle();
                        Red.setFillForegroundColor(HSSFColor.RED.index);
                        Red.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);


                            for (Row myrow : sheet) {


                                Cell val = myrow.getCell(4);
                                String cellval = val.toString();

                                Cell firstval = myrow.getCell(8);
                                String cellFirstValue = firstval.toString();

                                Cell firstcell = myrow.getCell(0);
                                String firstcelllstr = firstcell.toString();


                                for (int g = 0; g < array.size(); g++) {

                                    if ((cellval.equalsIgnoreCase(array.get(g)) && (cellFirstValue.equalsIgnoreCase(text)))
                                            & (firstcelllstr.equalsIgnoreCase("Social Assets"))) {
                                        count++;
                                        roww = sheet2.createRow(count);
                                        for (int f = 0; f < 8; f++) {
                                            Cell celll = roww.createCell(f);
                                            celll.setCellValue(String.valueOf(myrow.getCell(f)));
                                            //Toast.makeText(getApplicationContext(), " " + celll, Toast.LENGTH_SHORT).show();
                                            String valpublishing = celll.getStringCellValue();

                                            if (((flag == 1) & (f == 7)) | ((flag == 1) & (f == 6))) {
                                                celll.setCellStyle(GreenCell);
                                            }

                                            if (((flag == 2) & (f == 7)) | ((flag == 2) & (f == 6))) {
                                                celll.setCellStyle(YellowCell);
                                            }

                                            if (((flag == 3) & (f == 7)) | ((flag == 3) & (f == 6))) {
                                                celll.setCellStyle(Red);
                                            }

                                            if ((f == 3) & (valpublishing.equalsIgnoreCase("Done"))) {
                                                flag = 1;
                                            } else {
                                                if ((f == 3) & (valpublishing.equalsIgnoreCase("Ongoing"))) {
                                                    flag = 2;
                                                } else {
                                                    if ((f == 3) & (valpublishing.equalsIgnoreCase("Pending"))) {
                                                        flag = 3;
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        for (Row myrow : sheet) {


                            Cell val = myrow.getCell(4);
                            String cellval = val.toString();

                            Cell firstval = myrow.getCell(8);
                            String cellFirstValue = firstval.toString();

                            Cell firstcell = myrow.getCell(0);
                            String firstcelllstr = firstcell.toString();

                            for (int g = 0; g < array.size(); g++) {

                                if ((cellval.equalsIgnoreCase(array.get(g)) & (cellFirstValue.equalsIgnoreCase(text)))) {
                                    if (firstcelllstr.equalsIgnoreCase("Website")) {
                                        count++;
                                        roww = sheet2.createRow(count);
                                        for (int f = 0; f < 8; f++) {
                                            Cell celll = roww.createCell(f);
                                            celll.setCellValue(String.valueOf(myrow.getCell(f)));
                                            //Toast.makeText(getApplicationContext(), " " + celll, Toast.LENGTH_SHORT).show();
                                            String valpublishing = celll.getStringCellValue();

                                            if (((flag == 1) & (f == 7)) | ((flag == 1) & (f == 6))) {
                                                celll.setCellStyle(GreenCell);
                                            }

                                            if (((flag == 2) & (f == 7)) | ((flag == 2) & (f == 6))) {
                                                celll.setCellStyle(YellowCell);
                                            }

                                            if (((flag == 3) & (f == 7)) | ((flag == 3) & (f == 6))) {
                                                celll.setCellStyle(Red);
                                            }

                                            if ((f == 3) & (valpublishing.equalsIgnoreCase("Done"))) {
                                                flag = 1;
                                            } else {
                                                if ((f == 3) & (valpublishing.equalsIgnoreCase("Ongoing"))) {
                                                    flag = 2;
                                                } else {
                                                    if ((f == 3) & (valpublishing.equalsIgnoreCase("Pending"))) {
                                                        flag = 3;
                                                    }

                                                }
                                            }
                                        }


                                    }
                                }

                            }

                        }


                           int val= sheet2.getLastRowNum();
                            int val2=sheet2.getPhysicalNumberOfRows();
                        int last_social=0;
                        int lastwebite=0;
                        int Firstval_Asset_social=0;
                        int firestval_website=0;
                        int websiteasset_count=0;
                        int socialasset_count=0;


                        for (int i=8; i<sheet2.getPhysicalNumberOfRows()  ; i++) {
                            Cell cell = sheet2.getRow(i).getCell(0);
                            String Stringc = cell.toString();

                            if (cell != null) {
                                if (sheet2.getRow(i).getCell(0).toString().equals(sheet2.getRow(i + 1).getCell(0).toString()) & (sheet2.getRow(i).getCell(0).toString().equals("Social Assets"))

                                ) {
                                    last_social = i;
                                    if (Firstval_Asset_social == 0) {
                                        socialasset_count = i;
                                        Firstval_Asset_social++;
                                    }


                                } else {
                                    if (sheet2.getRow(i).getCell(0).toString().equals(sheet2.getRow(i + 1).getCell(0).toString()) & (sheet2.getRow(i).getCell(0).toString().equals("Website"))) {
                                        lastwebite = i;
                                        if (firestval_website == 0) {
                                            websiteasset_count = i;
                                            firestval_website++;
                                        }
                                    }
                                }
                            }}
                            if (Firstval_Asset_social != 0) {
                                    CellRangeAddress cellRangeAddress = new CellRangeAddress(socialasset_count, last_social + 1, 0, 0);
                                    sheet2.addMergedRegion(cellRangeAddress);
                            }
                            if (firestval_website != 0) {
                                for (int j = websiteasset_count; j <= websiteasset_count; j++) {
                                    CellRangeAddress cellRangeAddress1 = new CellRangeAddress(j, lastwebite + 1, 0, 0);
                                    sheet2.addMergedRegion(cellRangeAddress1);
                                }
                            }


                        StorageReference mStorageRef;
                        mStorageRef = FirebaseStorage.getInstance().getReference();

                        //FirebaseStorage storage = FirebaseStorage.getInstance();
                        //StorageReference storageReference = storage.getReferenceFromUrl("gs://tutsplus-firebase.appspot.com").child("test.txt");
                        Uri ffile = Uri.fromFile(new File(getExternalFilesDir(null), "extracted.xls"));

                        StorageReference riversRef = mStorageRef.child("Extracted_excel_Files/extracted.xls");

                        riversRef.putFile(ffile)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                        Toast.makeText(getApplicationContext(), "File Uploaded successfully", Toast.LENGTH_LONG).show();
                                        // Get a URL to the uploaded content
                                       //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                        // ...
                                    }
                                });



                        inputStream2.close();
                        File file = new File(getExternalFilesDir(null), "extracted.xls");
                        FileOutputStream outputStream = null;

                        try {
                            outputStream = new FileOutputStream(file);
                            workbook2.write(outputStream);
                            Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();

                            Toast.makeText(getApplicationContext(), "NOT OK", Toast.LENGTH_LONG).show();
                            try {
                                outputStream.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } catch (IOException | EncryptedDocumentException
                            | InvalidFormatException ex) {
                        ex.printStackTrace();
                    }
                }



        }

    @Override
    public void applyTexts(String username, String password, String fromString, String ccString) {

        Workbook wb = new HSSFWorkbook();
        Cell cell = null;
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);


        //Black color

        CellStyle blackcell = wb.createCellStyle();
        blackcell.setFillForegroundColor(HSSFColor.BLACK.index);
        blackcell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        //Second row color is black and font is white
        Font font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setColor(IndexedColors.WHITE.getIndex());
        blackcell.setFont(font);


        // cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        // cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        //Now we are creating sheet
        Sheet sheet = null;
        sheet = wb.createSheet("Name of sheet");
        //Now column and row
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        //Adding first row

        Row row1 = sheet.createRow(1);
        Cell cell1 = row1.createCell(2);
        cell1.setCellValue("DATE    "+thisDate);
        //Merging cells by providing cell index
        sheet.addMergedRegion(new CellRangeAddress(1,1,2,6));

        //Ading Second row






        Row row2 = sheet.createRow(2);
        Cell statuscell1 = row2.createCell(1);
        statuscell1.setCellValue("");
        statuscell1.setCellStyle(blackcell);

        Cell cell2 = row2.createCell(2);
        cell2.setCellValue("STATUS UPDATE   ");
        cell2.setCellStyle(blackcell);
        //Merging cells by providing cell index
        sheet.addMergedRegion(new CellRangeAddress(2,2,2,6));



        //row 3
        Row row3 = sheet.createRow(3);
        Cell row3cellone = row3.createCell(0);
        row3cellone.setCellValue("Client  : "+ username);

        Row row4 = sheet.createRow(4);
        Cell row4cellone = row4.createCell(0);
        row4cellone.setCellValue("To     :  "+ password);

        Row row5 = sheet.createRow(5);
        Cell row5cellone = row5.createCell(0);
        row5cellone.setCellValue("From   :  "+ fromString);

        Row row6 = sheet.createRow(6);
        Cell row6cellone = row6.createCell(0);
        row6cellone.setCellValue("CC     :  "+ ccString);



        Row row = sheet.createRow(7);

       /* cell = row.createCell(0);
        cell.setCellValue(" ");
        cell.setCellStyle(cellStyle);*/


        cell = row.createCell(0);
        cell.setCellValue("Project");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue("Task");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(2);
        cell.setCellValue("Visuals");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(3);
        cell.setCellValue("Publishing Timings");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(4);
        cell.setCellValue("Deadline");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(5);
        cell.setCellValue("FeedBack");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(6);
        cell.setCellValue("Status");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(7);
        cell.setCellValue("Action Plan");
        cell.setCellStyle(cellStyle);

        sheet.setColumnWidth(0, (10 * 300));
        sheet.setColumnWidth(1, (10 * 400));
        sheet.setColumnWidth(2, (10 * 300));
        sheet.setColumnWidth(3, (10 * 500));
        sheet.setColumnWidth(4, (10 * 300));
        sheet.setColumnWidth(5, (10 * 500));
        sheet.setColumnWidth(6, (10 * 300));
        sheet.setColumnWidth(7, (10 * 400));

        File file = new File(getExternalFilesDir(null), "extracted.xls");
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            wb.write(outputStream);
            Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "NOT OK", Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public void createNewFile() {




        Workbook wb = new HSSFWorkbook();
        Cell cell = null;
        CellStyle cellStyle = wb.createCellStyle();

        cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);




        // cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        // cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //Now we are creating sheet
        Sheet sheet = null;
        sheet = wb.createSheet("Name of sheet");
        //Now column and row
        Row row = sheet.createRow(0);

       /* cell = row.createCell(0);
        cell.setCellValue(" ");
        cell.setCellStyle(cellStyle);*/


        cell = row.createCell(0);
        cell.setCellValue("Project");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue("Task");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(2);
        cell.setCellValue("Visuals");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(3);
        cell.setCellValue("Publishing Timings");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(4);
        cell.setCellValue("Deadline");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(5);
        cell.setCellValue("FeedBack");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(6);
        cell.setCellValue("Status");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(7);
        cell.setCellValue("Action Plan");
        cell.setCellStyle(cellStyle);

        sheet.setColumnWidth(0, (10 * 300));
        sheet.setColumnWidth(1, (10 * 400));
        sheet.setColumnWidth(2, (10 * 300));
        sheet.setColumnWidth(3, (10 * 500));
        sheet.setColumnWidth(4, (10 * 300));
        sheet.setColumnWidth(5, (10 * 500));
        sheet.setColumnWidth(6, (10 * 300));
        sheet.setColumnWidth(7, (10 * 400));

        File file = new File(getExternalFilesDir(null), "extracted.xls");
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            wb.write(outputStream);
            Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "NOT OK", Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public void spiner() {
        categories = new ArrayList<String>();

        try {
            FileInputStream inputStream = new FileInputStream(new File(getExternalFilesDir(null), "master.xls"));
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (Row myrow : sheet) {
                //categories.clear();
                Cell val2 = myrow.getCell(8);
                String cellval2 = val2.toString();
                categories.add(cellval2);
            }
                Set<String> s = new LinkedHashSet<String>(categories);
                categories.clear();
                categories.addAll(s);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // attaching data adapter to spinner
                company.setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(),""+s,Toast.LENGTH_SHORT).show();
        } catch (IOException | EncryptedDocumentException
                | InvalidFormatException ex) {
            ex.printStackTrace();
        }
    }

    public void extractdata_merge(){

    }
}
