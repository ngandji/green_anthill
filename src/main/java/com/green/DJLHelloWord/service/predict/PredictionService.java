/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.green.DJLHelloWord.service.predict;

import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import com.green.DJLHelloWord.basic.Mlp;
import com.green.DJLHelloWord.utils.Utils;
import static com.green.DJLHelloWord.utils.Utils.MODEL_DIRE;
import static com.green.DJLHelloWord.utils.Utils.MODEL_NAME;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

/**
 *
 * @author Green
 */
@Service("predictionService")
public class PredictionService {
    
    public Classifications imageClassification() throws IOException, ModelException, TranslateException{
        
        Image img = ImageFactory.getInstance()
                .fromFile(Paths.get(Utils.IMAGE_FILE));
        
        try(Model model = Model.newInstance(Utils.MODEL_NAME)){
            
            model.setBlock(new Mlp(28*28, 10, new int[]{128, 64}));
            
            // assume that you have train and save your model in build/model floder.
            model.load(Paths.get(MODEL_DIRE));
            
            List<String> classes = IntStream.range(0, 10)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.toList());
            
            Translator<Image, Classifications> translator = 
                    ImageClassificationTranslator.builder()
                            .addTransform(new ToTensor())
                            .optSynset(classes)
                            .build();
            
            try(Predictor<Image, Classifications> predictor = model.newPredictor(translator)){
                return predictor.predict(img);
            }
        }
    }

    public Classifications imageClassification2() throws IOException, ModelException, TranslateException{
        Image img = ImageFactory.getInstance().fromUrl("https://resources.djl.ai/images/0.png");
        img.getWrappedImage();
        System.out.println("ok herre");
        Model model = Model.newInstance(MODEL_NAME);
            
        model.setBlock(new Mlp(28*28, 10, new int[]{128, 64}));
            
        // assume that you have train and save your model in build/model floder.
        model.load(Paths.get(MODEL_DIRE));
        
        Translator<Image, Classifications> translator = new Translator<Image, Classifications>(){
            @Override
            public NDList processInput(TranslatorContext tc, Image input) throws Exception {
                // Convert Image to NDArray
                NDArray array = input.toNDArray(tc.getNDManager(), Image.Flag.GRAYSCALE);
                return new NDList(NDImageUtils.toTensor(array));
            }

            @Override
            public Classifications processOutput(TranslatorContext tc, NDList ndlist) throws Exception {
//                 Create a Classifications with the output probabilities
                NDArray probabilities = ndlist.singletonOrThrow().softmax(0);
                List<String> classNames = IntStream.range(0, 10).mapToObj(String::valueOf).collect(Collectors.toList());
                return new Classifications(classNames, probabilities);
            }
            
            @Override
            public Batchifier getBatchifier() {
                // The Batchifier describes how to combine a batch together
                // Stacking, the most common batchifier, takes N [X1, X2, ...] arrays to a single [N, X1, X2, ...] array
                return Batchifier.STACK;
            }

        };
        
        Predictor<Image, Classifications> predictor = model.newPredictor(translator);
        
        Classifications classifications = predictor.predict(img);
        
        return classifications;
    }
}
