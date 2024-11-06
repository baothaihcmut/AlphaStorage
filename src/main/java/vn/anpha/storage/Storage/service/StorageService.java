package vn.anpha.storage.Storage.service;

import org.apache.catalina.util.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;


import java.util.Map;
import java.util.HashMap;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketVersioningArgs;
import io.minio.http.Method;
import io.minio.messages.VersioningConfiguration;

import java.util.concurrent.TimeUnit;
@Service
public class StorageService {
    @Autowired
    private MinioClient minioClient;

    public void createBucket(String bucketName,boolean version) throws HttpClientErrorException, Exception{
        boolean bucketExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if(bucketExist) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT,"Bucket name exists");
        }
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        if(version) {
            minioClient.setBucketVersioning(
                SetBucketVersioningArgs.builder()
                                       .bucket(bucketName)
                                       .config(new VersioningConfiguration(VersioningConfiguration.Status.ENABLED,null))
                                       .build()
            );
        }
    }

    public String getPresignUrl(String bucketName, String objectName,int expireration) throws HttpServerErrorException{
        try {
            return this.minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                                         .method(Method.GET)
                                         .bucket(bucketName)
                                         .object(objectName)
                                         .expiry(expireration,TimeUnit.MINUTES)
                                        //  .extraQueryParams(param)
                                         .build()
                );
        }
        catch (Exception e) {
            System.err.println(e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
