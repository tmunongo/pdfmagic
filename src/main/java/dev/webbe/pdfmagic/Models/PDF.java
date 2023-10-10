package dev.webbe.pdfmagic.Models;

import lombok.Data;

@Data
public class PDF {
    private Long id;

    private String fileName;

    private String location;

    private int pageCount;

    public PDF(String fileName, String location, int pageCount) {
        this.fileName = fileName;
        this.location = location;
        this.pageCount = pageCount;
    }
}