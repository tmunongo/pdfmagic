package dev.webbe.pdfmagic.Services;

import java.io.File;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

@Service
public class PDFConversionService {
    public PDDocument convertMulitpartFileToPD(File fileToConvert) {
        try {
            return Loader.loadPDF(fileToConvert);
        } catch (Exception e) {
            return null;
        }
    }
}
