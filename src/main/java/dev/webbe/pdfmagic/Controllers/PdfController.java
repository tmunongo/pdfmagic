package dev.webbe.pdfmagic.Controllers;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.webbe.pdfmagic.Services.FileManipulatorService;

@RestController
@RequestMapping(value = "/api")
public class PdfController {
    FileManipulatorService fileManipulator = new FileManipulatorService();
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello User!";
    }

    @PostMapping("/merge-pdf")
    public String mergeHandler(@RequestParam("file1") MultipartFile fileA, @RequestParam("file2") MultipartFile fileB) throws IOException {
        if (fileA.isEmpty() || fileB.isEmpty()) {
            return "must provide two files";
        } else {
            String result = fileManipulator.merge(fileA, fileB);
        
             return result;
        }
    }

    @PostMapping("/split-pdf")
    public String splitHandler(@RequestParam("file") MultipartFile file, @RequestParam("pageNumber") int pageNumber) throws IOException {
        if (file.isEmpty()) {
            return "A file must be specified";
        } else if (pageNumber == 0) {
            return "cannot find page number";
        } else {
            String result = fileManipulator.split(file, pageNumber);

            return result;
        }

    }

    @PostMapping("/extract-pages")
    public String extractPagesHandler(@RequestParam("file") MultipartFile file, @RequestParam("startPage") int startPage, @RequestParam("endPage") int endPage) throws IOException {
        if (file.isEmpty()) {
            return "Must supply a file";
        } else {
            String result = fileManipulator.extractPages(file, startPage, endPage);

            return result;
        }
    }
    
}