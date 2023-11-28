package com.example.demo.model;

public class FileInformation implements Comparable {
    String fileurl;
    String filename;
    String modifyTime;
    String dictionary;


    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getDictionary() {
        return dictionary;
    }

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public int compareTo(Object o) {
        FileInformation fileInformation = (FileInformation) o;
        return -modifyTime.compareTo(fileInformation.getModifyTime());
    }
}
