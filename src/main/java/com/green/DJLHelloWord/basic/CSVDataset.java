/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.green.DJLHelloWord.basic;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.dataset.Record;
import ai.djl.translate.TranslateException;
import ai.djl.util.Progress;
import static com.green.DJLHelloWord.utils.Utils.ANSWER;
import static com.green.DJLHelloWord.utils.Utils.PATH_DATASET;
import static com.green.DJLHelloWord.utils.Utils.QUESTION;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author Green
 */
public class CSVDataset extends RandomAccessDataset{

    private final List<CSVRecord> csvRecords;
    
    private CSVDataset(Builder builder) {
        super(builder);
        this.csvRecords = builder.dataset;
    }
    
    @Override
    public Record get(NDManager manager, long index){
        CSVRecord record = csvRecords.get(Math.toIntExact(index));
        NDArray datum = manager.create(encode(record.get(QUESTION)));
        NDArray label = manager.create(Float.parseFloat(record.get(ANSWER)));
        
        return new Record(new NDList(datum), new NDList(label));
    }

    @Override
    protected long availableSize() {
        return this.csvRecords.size();
    }

    @Override
    public void prepare(Progress prgrs) throws IOException, TranslateException {
    }
    
    private int[] encode(String url){
        url = url.toLowerCase();
        int[] encoding = new int[26];
        for(char ch: url.toCharArray()){
            int index = ch - 'a';
            if(index < 26 && index >= 0 )
                encoding[ch-'a']++;
        }
        return encoding;
    }
    
    public static Builder builder(){
        return new Builder();
    }
    
    public static final class Builder extends BaseBuilder<Builder>{
        private Usage usage;
        List<CSVRecord> dataset;
        
        Builder (){
            this.usage = Usage.TRAIN;
        }
        
        @Override
        protected Builder self() {
            return this;
        }
        
        Builder optUsage(Usage usage){
            this.usage = usage;
            return this;
        }
        
        CSVDataset build() throws IOException{
            String csvFilePath  = PATH_DATASET;
            
            try(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
                CSVParser csvParser = new CSVParser(
                        reader,
                        CSVFormat.DEFAULT
                            .withHeader(QUESTION, ANSWER)
                            .withFirstRecordAsHeader()
                            .withIgnoreHeaderCase()
                            .withTrim())){
                List<CSVRecord> csvRecords = csvParser.getRecords();
                
                int index = (int)(csvRecords.size()*0.8);
                switch(usage){
                    case TRAIN:{
                        dataset = csvRecords.subList(0, index);
                        break;
                    }
                    case TEST:{
                        dataset = csvRecords.subList(index, csvRecords.size());
                        break;
                    }
                }
            }
            return new CSVDataset(this);
        }
    }
    
    /*
        int batchSize = 128;
        CSVDataset csvDataset = new CSVDataset.Builder().optUsage(Usage.TRAIN).setSampling(batchSize, true).build();
    //*/
}
