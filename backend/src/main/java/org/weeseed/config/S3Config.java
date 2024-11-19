package org.weeseed.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.regions.Region;

@Configuration
public class S3Config {

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

//    @PostConstruct
//    public void checkEnvVariables() {
//        System.out.println("AWS Access Key: " + accessKey);
//        System.out.println("AWS Secret Key: " + secretKey);
//        System.out.println("AWS Region: " + region);
//        System.out.println("System CLOUD_AWS_REGION: " + System.getenv("CLOUD_AWS_REGION"));
//        System.out.println("System AWS_REGION: " + System.getenv("AWS_REGION"));
//    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.of(region))
                .build();
    }
}
