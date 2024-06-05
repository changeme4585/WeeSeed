package com.example.WeeSeed.ImageAi;


import com.example.WeeSeed.service.AacService;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.ResNet50;
import org.deeplearning4j.zoo.model.VGG16;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.ops.transforms.Transforms;

import java.io.IOException;

public class ImageDistanceCalculator {
    public static double calculateImageDistance(INDArray image1, INDArray image2) throws IOException {
//        ZooModel<ComputationGraph> model = AacService.model;
//        ComputationGraph resNet50 = AacService.resNet50;

        ZooModel<ComputationGraph> model =  ResNet50.builder().build();
        ComputationGraph resNet50 = (ComputationGraph) model.initPretrained();

        // 이미지 스케일링 및 전처리
        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0, 1);
        scaler.transform(image1);
        scaler.transform(image2);

        // 이미지 특징 추출
        INDArray features1 = resNet50.outputSingle(image1);
        INDArray features2 = resNet50.outputSingle(image2);

        double sumOfSquaredDifferences = features1.squaredDistance(features2);
        double euclideanDistance = Math.sqrt(sumOfSquaredDifferences);

        return euclideanDistance;
    }





}
