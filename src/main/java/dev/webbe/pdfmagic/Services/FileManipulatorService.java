package dev.webbe.pdfmagic.Services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.PageExtractor;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdfwriter.compress.CompressParameters;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileManipulatorService {
    @Value("${merged.pdf.destination}")
    private String destinationFolder;
    
    PDFMergerUtility pdfMerger = new PDFMergerUtility();
    Splitter pdfSplitter = new Splitter();

    public String split(MultipartFile file, int splitPage) throws IOException {
        pdfSplitter.setStartPage(1);
        pdfSplitter.setEndPage(splitPage);

        List<PDDocument> documents = pdfSplitter.split((PDDocument) file);

        System.out.println(documents);

        return "File: " + file + " split successfully!";
    }

    public String merge(MultipartFile file1, MultipartFile file2) throws IOException {
        // generate a random filename
            File tempFile1 = File.createTempFile("temp1", ".pdf");
            File tempFile2 = File.createTempFile("temp2", ".pdf");
    
            try {
                InputStream fileAStream = file1.getInputStream();
                InputStream fileBStream = file2.getInputStream();
                
                Files.copy(fileAStream, tempFile1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(fileBStream, tempFile2.toPath(), StandardCopyOption.REPLACE_EXISTING);
                // TODO: create a hash temporary name for the merged file
                
                pdfMerger.setDestinationFileName(destinationFolder + "merged.pdf");

                pdfMerger.addSource(tempFile1);
                pdfMerger.addSource(tempFile2);

                pdfMerger.mergeDocuments(null, null);

                System.out.println("Merged documents");
                return destinationFolder + "merged.pdf";
            } catch (Exception e) {
                // TODO: handle exception
                return "Failed to merge documents because " + e.getMessage();
            } finally {
                tempFile1.delete();
                tempFile2.delete();
                System.out.println("Files deleted");
            }
    }

    public String extractPages(MultipartFile file, int startPage, int endPage) throws IOException {
        File tempFile = File.createTempFile("temp1", ".pdf");
        PDFConversionService pdfConversionService = new PDFConversionService();
        
        try {
            InputStream fileStream = file.getInputStream();
            
            Files.copy(fileStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            PDDocument document = pdfConversionService.convertMulitpartFileToPD(tempFile);

            PageExtractor extractor = new PageExtractor(document, startPage, endPage);

            // extractor.setStartPage(startPage);
            // extractor.setEndPage(endPage);
            
            
            PDDocument outputFile = extractor.extract();
            
            outputFile.save(destinationFolder + "output.pdf", CompressParameters.NO_COMPRESSION);
            
            return "Extacted";
        } catch (Exception e) {
            // TODO: handle exception
            return "Failed to extract because: " + e.getMessage();
        }
    }
}
