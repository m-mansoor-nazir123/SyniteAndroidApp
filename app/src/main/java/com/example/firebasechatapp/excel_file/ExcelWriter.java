package com.example.firebasechatapp.excel_file;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.firebasechatapp.AddActivity;
import com.example.firebasechatapp.ExampleDialog;
import com.example.firebasechatapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ExcelWriter extends AppCompatActivity {
EditText project;
EditText Task;
    EditText Visuals;
    EditText PublishingTimeline;
    EditText Deadline;
    EditText Feedback;
    EditText Status;
    EditText ActionPlan;
    Button ExtractBut;
    Button excelfile;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    Spinner company;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_writer);

        excelfile = findViewById(R.id.excelfile);
        ExtractBut=findViewById(R.id.queryData);
        company=findViewById(R.id.project);
        Task=findViewById(R.id.Task);
        Visuals=findViewById(R.id.Visuals);
        PublishingTimeline=findViewById(R.id.PublishingTimings);
        Deadline=findViewById(R.id.Deadline);
        Feedback=findViewById(R.id.Feedback);
        Status=findViewById(R.id.Status);
        ActionPlan=findViewById(R.id.ActionPlan);

        final Calendar myCalendar = Calendar.getInstance();
        spiner();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Deadline.setText(sdf.format(myCalendar.getTime()));

            }

        };

        Deadline.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ExcelWriter.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        excelfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyExistingWorkbook(company,Task,Visuals,PublishingTimeline,Deadline,Feedback,Status,ActionPlan);
            }
        });
        ExtractBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExcelWriter.this, ExtractActivity.class));
            }
        });

        File file = new File(getExternalFilesDir(null), "plik.xls");
        if(file.exists()) {
            Toast.makeText(getApplicationContext(),"file Exist",Toast.LENGTH_SHORT).show();
        }
        else {
            //openDialog();
            StorageReference storage = FirebaseStorage.getInstance().getReference();
            StorageReference islandRef = storage.child("Main_excel_Files/plix.xls");

           /* File rootPath = new File(Environment.getExternalStorageDirectory(), "file_name");
            if(!rootPath.exists()) {
                rootPath.mkdirs();
            }*/
            Uri ffile = Uri.fromFile(new File(getExternalFilesDir(null), "plik.xls"));

            islandRef.getFile(ffile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Log.e("firebase ",";local tem file created  created " +localFile.toString());
                    Toast.makeText(getApplicationContext(),"File downloaded from firebse",Toast.LENGTH_SHORT).show();
                    //  updateDb(timestamp,localFile.toString(),position);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                   // Log.e("firebase ",";local tem file not created  created " +exception.toString());

                    createNewFile();
                }
            });
           // createNewFile();
        }


    }

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }



    private void modifyExistingWorkbook(Spinner t, EditText twoCol, EditText threecol, EditText fourcol, EditText fivecol, EditText sixcol, EditText sevencol, EditText eightcol)  {
        {
String excelFilePath = "plik.xls";

try {
FileInputStream inputStream = new FileInputStream(new File(getExternalFilesDir(null), "plik.xls"));
Workbook workbook = WorkbookFactory.create(inputStream);
Sheet sheet = workbook.getSheetAt(0);
    String edit=t.getSelectedItem().toString();
    String twoColVal=twoCol.getText().toString();
    String threecolVal=threecol.getText().toString();
    String fourcolVal=fourcol.getText().toString();
    String FivecolVal=fivecol.getText().toString();
    String SixColVal=sixcol.getText().toString();
    String SevenColVal=sevencol.getText().toString();
    String EigthColVal=eightcol.getText().toString();


Object[][] bookData = {
{edit,twoColVal,threecolVal,fourcolVal,FivecolVal,SixColVal,SevenColVal,EigthColVal}
};

int rowCount = sheet.getLastRowNum();

for (Object[] aBook : bookData) {
Row row = sheet.createRow(++rowCount);
int columnCount = 0;

for (Object field : aBook) {

Cell cell = row.createCell(columnCount);
if (field instanceof String) {
cell.setCellValue((String) field);
} else if (field instanceof Integer) {
cell.setCellValue((Integer) field);
}
columnCount++;
}
}


    StorageReference mStorageRef;
    mStorageRef = FirebaseStorage.getInstance().getReference();

    //FirebaseStorage storage = FirebaseStorage.getInstance();
    //StorageReference storageReference = storage.getReferenceFromUrl("gs://tutsplus-firebase.appspot.com").child("test.txt");
    Uri ffile = Uri.fromFile(new File(getExternalFilesDir(null), "plik.xls"));



    StorageReference riversRef = mStorageRef.child("Main_excel_Files/plix.xls");

    riversRef.putFile(ffile)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(getApplicationContext(), "File Uploaded successfully", Toast.LENGTH_LONG).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    // ...
                }
            });







    inputStream.close();

    File file = new File(getExternalFilesDir(null), "plik.xls");
    FileOutputStream outputStream = null;

    try {
        outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
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

        File file = new File(getExternalFilesDir(null), "plik.xls");
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

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("data");

        final ArrayList<String> categories = new ArrayList<String>();

        databaseReference.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //AddData addData=new AddData();
                    String id=ds.getKey();
                    Map<Object, String> map = (Map<Object, String>) dataSnapshot.child(id).getValue();
                    String string= map.get("nameData");
                    categories.add(string);
                    //dataAdapter.notifyDataSetChanged();
                }
                Set<String> s = new LinkedHashSet<String>(categories);
                categories.clear();
                categories.addAll(s);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // attaching data adapter to spinner
                company.setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(),""+s,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


}

