package dev.webbe.pdfmagic.Controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class PdfController {
    @Value("${merged.pdf.destination}")
    private String destinationFolder;
    
    PDFMergerUtility pdfMerger = new PDFMergerUtility();
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello User!";
    }

    @PostMapping("/split-pdf")
    public String split(@RequestParam("file1") MultipartFile fileA, @RequestParam("file2") MultipartFile fileB) throws IOException {
        if (fileA.isEmpty() || fileB.isEmpty()) {
            return "must provide two files";
        } else {
            // generate a random filename
            File tempFile1 = File.createTempFile("temp1", ".pdf");
            File tempFile2 = File.createTempFile("temp2", ".pdf");
    
            try {
                InputStream fileAStream = fileA.getInputStream();
                InputStream fileBStream = fileB.getInputStream();
                
                Files.copy(fileAStream, tempFile1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(fileBStream, tempFile2.toPath(), StandardCopyOption.REPLACE_EXISTING);
                // TODO: create a hash temporary name for the merged file
                pdfMerger.setDestinationFileName(destinationFolder + "merged.pdf");

                pdfMerger.addSource(tempFile1);
                pdfMerger.addSource(tempFile2);

                pdfMerger.mergeDocuments(null, null);

                System.out.println("Merged documents");
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("Failed to merge documents because " + e.getMessage());
            } finally {
                tempFile1.delete();
                tempFile2.delete();
                System.out.println("Files deleted");
            }
        
             return "Successfully merged files";
        }
    }
    
}