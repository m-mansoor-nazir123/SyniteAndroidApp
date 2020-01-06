package com.example.firebasechatapp.AddData;

public class AddData {

    private String Company;
    private  String ThemeSelected;
    private String Url;
    private String Notes;

    public AddData( ) {

    }

    public AddData(String company, String themeSelected, String url, String notes) {
        Company = company;
        ThemeSelected = themeSelected;
        Url = url;
        Notes = notes;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getThemeSelected() {
        return ThemeSelected;
    }

    public void setThemeSelected(String themeSelected) {
        ThemeSelected = themeSelected;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }
}