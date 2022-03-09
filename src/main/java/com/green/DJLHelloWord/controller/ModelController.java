/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.green.DJLHelloWord.controller;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import com.green.DJLHelloWord.service.TestAccessToDataset;
import com.green.DJLHelloWord.service.predict.PredictionService;
import com.green.DJLHelloWord.service.training.TrainModelService;
import com.green.DJLHelloWord.utils.Message;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Green
 */
@RestController
@RequestMapping("api/djl")
@CrossOrigin(origins = "*")
public class ModelController {
    @Autowired
    private TrainModelService trainModelService;
    
    @Autowired
    private PredictionService predictionService;
    
    @Autowired
    private TestAccessToDataset datasetAccess;
    
    @GetMapping("train_mxnet")
    public ResponseEntity<Message> trainMnist() throws IOException, TranslateException{
        trainModelService.trainMnist();
        return ResponseEntity.ok(new Message("Train Mnist is sucesful !"));
    }
    
    @GetMapping("train_csv")
    public ResponseEntity<Message> trainCSVDataSet() throws IOException, TranslateException{
        trainModelService.trainCSVDataset();
        return ResponseEntity.ok(new Message("Train CSV Dataset is sucesful !"));
    }
    
    @GetMapping("image_classification")
    public ResponseEntity<Message> predict() throws IOException, ModelException, TranslateException{
        this.predictionService.imageClassification2();
        return ResponseEntity.ok(new Message(""));
    }
    
    @GetMapping("test")
    public ResponseEntity<Message> testAccessToDataset() throws IOException{
        return ResponseEntity.ok(new Message(this.datasetAccess.readResourceFile()));
    }
    
    @GetMapping("hello")
    public ResponseEntity<Message> hello(){
        return ResponseEntity.ok(new Message("Hello"));
    }
}