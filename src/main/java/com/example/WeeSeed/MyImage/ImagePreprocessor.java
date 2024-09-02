package com.example.WeeSeed.MyImage;

import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.transform.ResizeImageTransform;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;

import java.io.File;

public class ImagePreprocessor {
    private static final int HEIGHT = 224;
    private static final int WIDTH = 224;
    private static final int CHANNELS = 3;

    public static INDArray preprocessImage(File imageFile) throws Exception {
        NativeImageLoader loader = new NativeImageLoader(HEIGHT, WIDTH, CHANNELS);
        INDArray image = loader.asMatrix(imageFile);
        VGG16ImagePreProcessor preProcessor = new VGG16ImagePreProcessor();
        preProcessor.transform(image);
        return image;
    }

    public static INDArray extractFeatures(File imageFile, ComputationGraph model) throws Exception {
        INDArray image = preprocessImage(imageFile);
        INDArray[] output = model.output(false, image);
        return output[0];  // 특성 벡터
    }
}
