/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.green.DJLHelloWord.utils;

import ai.djl.engine.Engine;

/**
 *
 * @author Green
 */
public final class Utils {
    public static final int INPUT = 20*2+30*8;
    public static final int OUTPUT = 20;
    public static final int MAX_GPUS = 1;
    public static final int EPOCH = 2;
    public static final int BATCH_SIZE = 32;
    public static final int LIMIT = 1;
    
    public static final String MODEL_NAME = "mlp";
    public static final String MODEL_DIRE = "build/model";
    public static final String OUTPUT_DIRE = "mlp";
    
    public static final String QUESTION = "url";
    public static final String ANSWER = "isMalicious";
    public static final String PATH_DATASET = "C:\\uploads\\djl_dataset\\malicious_url_data.csv";
    public static final String IMAGE_FILE = "src/test/resources/0.png";
    
    private Utils(){}
    
}
