package com.example.helpgenic.Classes;

public class Document {
    public Document(int documentId, String documentName) {
        this.documentId = documentId;
        this.documentName = documentName;
    }

    private int documentId;
    private String documentName;

    public int getDocumentId() {
        return documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}
