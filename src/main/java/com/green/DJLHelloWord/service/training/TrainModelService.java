/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.green.DJLHelloWord.service.training;

import ai.djl.Model;
import ai.djl.basicdataset.cv.classification.Mnist;
import ai.djl.engine.Engine;
import ai.djl.metric.Metrics;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Block;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.TrainingResult;
import ai.djl.training.dataset.Dataset;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.listener.SaveModelTrainingListener;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import com.green.DJLHelloWord.basic.Mlp;
import com.green.DJLHelloWord.utils.Utils;
import static com.green.DJLHelloWord.utils.Utils.EPOCH;
import static com.green.DJLHelloWord.utils.Utils.INPUT;
import static com.green.DJLHelloWord.utils.Utils.MODEL_DIRE;
import static com.green.DJLHelloWord.utils.Utils.MODEL_NAME;
import static com.green.DJLHelloWord.utils.Utils.OUTPUT;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
/**
 *
 * @author Green
 */
@Service("traiModelService")
public class TrainModelService {
    
    public TrainingResult trainMnist() throws IOException, TranslateException{
        System.out.println("train mnist started");
        try(Model model = Model.newInstance(MODEL_NAME)){
            System.out.println("all is ok here");
            model.setBlock(new Mlp(Mnist.IMAGE_HEIGHT*Mnist.IMAGE_WIDTH, Mnist.NUM_CLASSES, new int[]{128, 64}));
            
            RandomAccessDataset trainingSet = this.getDataSet(Dataset.Usage.TRAIN, Utils.BATCH_SIZE, Utils.LIMIT);
            RandomAccessDataset validateSet = this.getDataSet(Dataset.Usage.TEST, Utils.BATCH_SIZE, Utils.LIMIT);
            
            try(Trainer trainer = model.newTrainer(this.setupTrainingConfig(MODEL_NAME, this.getGpus()))){
                trainer.setMetrics(new Metrics());
                trainer.initialize(new Shape(1, Mnist.IMAGE_HEIGHT* Mnist.IMAGE_WIDTH));
                EasyTrain.fit(trainer, Utils.EPOCH, trainingSet, validateSet);
                return trainer.getTrainingResult();
            }
        }
    }
    
    public TrainingResult trainCSVDataset() throws IOException, TranslateException{
        
        try(Model model = Model.newInstance(MODEL_NAME)){
            model.setBlock(new Mlp(INPUT, OUTPUT, new int[]{128, 64}));
            
            //get training and validation dataset
            RandomAccessDataset trainingSet = this.getDataSet(Dataset.Usage.TRAIN, Utils.BATCH_SIZE, Utils.LIMIT);
            RandomAccessDataset validateSet = this.getDataSet(Dataset.Usage.TEST, Utils.BATCH_SIZE, Utils.LIMIT);
            
            //set training configuration
            try(Trainer trainer = model.newTrainer(this.setupTrainingConfig(MODEL_NAME, this.getGpus()))){
                trainer.setMetrics(new Metrics());

                trainer.initialize(new Shape(1, INPUT));

                EasyTrain.fit(trainer, EPOCH, trainingSet, validateSet);
                
                Path modelDire = Paths.get(MODEL_DIRE);
                Files.createDirectories(modelDire);
                
                model.setProperty("Epoch", String.valueOf(EPOCH));
                model.save(modelDire, MODEL_NAME);
                
                return trainer.getTrainingResult();
            }
            
        }
    }
    
    private DefaultTrainingConfig setupTrainingConfig(String outputDire, int maxGpus){
        SaveModelTrainingListener listener = new SaveModelTrainingListener(outputDire);
        listener.setSaveModelCallback(
                trainer ->{
                    TrainingResult result = trainer.getTrainingResult();
                    Model model = trainer.getModel();
                    float accuracy = result.getTrainEvaluation("Accuracy");
                    model.setProperty("Accuracy", String.format("%.5f", accuracy));
                    model.setProperty("Loss", String.format("%.5f", result.getValidateLoss()));
                });
        
        return new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
                .addEvaluator(new Accuracy())
                .optDevices(Engine.getInstance().getDevices(maxGpus))
                .addTrainingListeners(TrainingListener.Defaults.logging(outputDire))
                .addTrainingListeners(listener);
    }
    
    private RandomAccessDataset getDataSet(Dataset.Usage usage, int batchSize, int limit) throws IOException{
        Mnist mnist = Mnist.builder()
                .optUsage(usage)
                .setSampling(batchSize, true)
                .optLimit(limit)
                .build();
        mnist.prepare(new ProgressBar());
        return mnist;
    }
    
    private int getGpus(){
        return Engine.getInstance().getGpuCount();
    }
    
}
