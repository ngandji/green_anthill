/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.green.DJLHelloWord.service;

import static com.green.DJLHelloWord.utils.Utils.PATH_DATASET;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Green
 */
@Service("testService")
public class TestAccessToDataset {
    
    public String readFile(){
        /*
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File((new ClassPathResource(PATH_DATASET)).getPath())));
        //*/
        try (BufferedReader br = new BufferedReader(new FileReader(new File((new ClassPathResource(PATH_DATASET)).getPath())))) {
            StringBuilder sb = new StringBuilder();
            String line;
            int count = 0;
            while(((line = br.readLine()) != null) && count < 10){
                sb.append(line);
                sb.append("\n");
                count++;
            }
            System.out.println("content file");
            System.out.print(sb.toString());
            return sb.toString();
        } catch (FileNotFoundException ex) {
            System.out.print(ex.getMessage());
            return ex.getMessage();
        } catch (IOException ex) {
            System.out.print(ex.getMessage());
            return ex.getMessage();
        }
    }

    public String readFile2() throws FileNotFoundException{
        File doc = new File("C:\\uploads\\djl_dataset\\malicious_url_data.csv");
        Scanner obj = new Scanner(doc);
        int count = 0;
        StringBuilder sb = new StringBuilder();
        while(obj.hasNextLine() && count < 10){
            sb.append(obj.nextLine());
            sb.append("\n");
            count++;
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
    
    public String readResourceFile() throws IOException{
        Resource resource = new ClassPathResource("/malicious_url_data.csv");
        Scanner obj = new Scanner(resource.getFile());
        int count = 0;
        StringBuilder sb = new StringBuilder();
        while(obj.hasNextLine() && count < 10){
            sb.append(obj.nextLine());
            sb.append("\n");
            count++;
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}
