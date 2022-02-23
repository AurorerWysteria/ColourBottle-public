package com.wysteria.colourbottle.object;

public class ARObject {
    private String objFilePath = "";
    private String objTexturePath = "";

    public ARObject() {

    }

    public ARObject(String objFilePath, String objTexturePath) {
        this.objFilePath = objFilePath;
        this.objTexturePath = objTexturePath;
    }

    public String getObjFilePath() {
        return objFilePath;
    }

    public void setObjFilePath(String objFilePath) {
        this.objFilePath = objFilePath;
    }

    public String getObjTexturePath() {
        return objTexturePath;
    }

    public void setObjTexturePath(String objTexturePath) {
        this.objTexturePath = objTexturePath;
    }
}