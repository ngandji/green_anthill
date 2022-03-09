/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.green.DJLHelloWord.basic;

import ai.djl.ndarray.NDList;
import ai.djl.nn.Activation;
import ai.djl.nn.Blocks;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.core.Linear;
import java.util.function.Function;

/**
 *
 * @author Green
 */
public class Mlp extends SequentialBlock{
    
    public Mlp(int input, int output, int[] hidden){
        this(input, output, hidden, Activation::relu);
    }
    public Mlp(int input, int output, int[] hidden, Function<NDList, NDList> activation){
        add(Blocks.batchFlattenBlock(input));
        for(int hiddenSize : hidden){
            add(Linear.builder().setUnits(hiddenSize).build());
            add(activation);
        }
        
        add(Linear.builder().setUnits(output).build());
    }
}
