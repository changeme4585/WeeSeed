package com.example.WeeSeed.MyImage;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.VGG16;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.zoo.PretrainedType;
import org.deeplearning4j.util.ModelSerializer;

public class ModelLoader {
    public static ComputationGraph loadVGG16Model() throws Exception {
        ZooModel zooModel = VGG16.builder().build();
        ComputationGraph model = (ComputationGraph) zooModel.initPretrained(PretrainedType.IMAGENET);
        return model;
    }
}
