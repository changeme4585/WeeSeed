package com.example.WeeSeed.MyImage;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ImageSimilarity {
    private static final int NUM_THREADS = 4; // 사용할 스레드 수

    public static List<INDArray> extractFeaturesInParallel(List<File> imageFiles, ComputationGraph model) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(imageFiles.size());
        List<Future<INDArray>> futures = new ArrayList<>();

        for (File imageFile : imageFiles) {
            futures.add(executorService.submit(() -> ImagePreprocessor.extractFeatures(imageFile, model)));
        }

        List<INDArray> featuresList = new ArrayList<>();
        for (Future<INDArray> future : futures) {
            featuresList.add(future.get());
        }

        executorService.shutdown();
        return featuresList;
    }

    public static double cosineSimilarity(INDArray vectorA, INDArray vectorB) {
        double dotProduct = Nd4j.getBlasWrapper().dot(vectorA, vectorB);
        double normA = vectorA.norm2Number().doubleValue();
        double normB = vectorB.norm2Number().doubleValue();
        return dotProduct / (normA * normB);
    }

    public static void findMostSimilarImageAsync(File queryImage, List<File> imageFiles, ComputationGraph model) throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
            try {
                INDArray queryFeatures = ImagePreprocessor.extractFeatures(queryImage, model);
                List<INDArray> featuresList = extractFeaturesInParallel(imageFiles, model);

                double highestSimilarity = -1.0;
                File mostSimilarImage = null;

                for (int i = 0; i < imageFiles.size(); i++) {
                    double similarity = cosineSimilarity(queryFeatures, featuresList.get(i));
                    if (similarity > highestSimilarity) {
                        highestSimilarity = similarity;
                        mostSimilarImage = imageFiles.get(i);
                    }
                }

                if (mostSimilarImage != null) {
                    System.out.println("가장 유사한 이미지: " + mostSimilarImage.getPath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executorService.shutdown();
    }

    public static void main(String[] args) throws Exception {
        // 모델 로드
        ComputationGraph model = ModelLoader.loadVGG16Model();

        // 이미지 파일 목록
        List<File> imageFiles = new ArrayList<>();
        imageFiles.add(new File("/Users/joseungbin/Desktop/dog.jpg"));
        imageFiles.add(new File("/Users/joseungbin/Desktop/스크린샷 2024-06-18 오후 8.39.04.png"));
        imageFiles.add(new File("/Users/joseungbin/Desktop/스크린샷 2024-06-18 오후 8.39.10.png"));
        imageFiles.add(new File("/Users/joseungbin/Downloads/photo_0_(1).jpg"));
        imageFiles.add(new File("/Users/joseungbin/Desktop/스크린샷 2024-06-18 오후 8.58.14.png"));
        // ... 여러 이미지 파일 추가 ...

        // 검색할 이미지 파일
        File queryImage = new File("/Users/joseungbin/Desktop/스크린샷 2024-06-18 오후 9.07.59.png");

        // 비동기 작업 시작
        findMostSimilarImageAsync(queryImage, imageFiles, model);

        // 다른 작업 수행 가능
        System.out.println("끝");
    }
}
