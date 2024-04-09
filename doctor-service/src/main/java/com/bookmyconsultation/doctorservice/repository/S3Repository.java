package com.bookmyconsultation.doctorservice.repository;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Repository {
    private AmazonS3 s3Client;
    private String BUCKET_NAME = "ruhan.bmc.doctor.documents"; // Configure before run
    // This needs to be a unique bucket name across all the regions.

    ObjectMetadata metadata;

    @PostConstruct
    public void init(){
        // Update the amazon s3 admin access key and secret key here once generated for your account
        String accessKey = ""; // Configure before run
        String secretKey = ""; // Configure before run
        AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    public void uploadFiles(String doctorId, MultipartFile file) throws IOException {
        String key = doctorId + "/"+ file.getOriginalFilename();
        if(!s3Client.doesBucketExistV2(BUCKET_NAME)){
            s3Client.createBucket(BUCKET_NAME);
        }
        s3Client.putObject(BUCKET_NAME,key,file.getInputStream(),metadata);
    }

    public List<String> listBucketObjects(String doctorId) {
        ListObjectsV2Result result = s3Client.listObjectsV2(BUCKET_NAME,doctorId + "/");
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        List<String> doctorDocuments = new ArrayList<>();
        for (S3ObjectSummary os : objects) {
            doctorDocuments.add(os.getKey().substring((doctorId.length()+1)));
        }
        return doctorDocuments;
    }

    public ByteArrayOutputStream getDoctorUploadedFile(String doctorId, String documentName) throws IOException {
        String keyName = doctorId + "/" + documentName;
        S3Object s3Object = s3Client.getObject(BUCKET_NAME, keyName);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] readBuf = new byte[1024];
        int readLen = 0;
        while ((readLen = s3ObjectInputStream.read(readBuf)) > 0) {
            byteArrayOutputStream.write(readBuf, 0, readLen);
        }
        s3ObjectInputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream;
    }
}
