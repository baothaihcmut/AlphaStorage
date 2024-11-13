package vn.anpha.storage.Storage.service;

import java.util.concurrent.TimeUnit;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.SetBucketVersioningArgs;
import io.minio.http.Method;
import io.minio.messages.VersioningConfiguration;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
public class StorageService {
    @Autowired
    private MinioClient minioClient;

    public void createBucket(String bucketName, boolean version) throws HttpClientErrorException, Exception {
        boolean bucketExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (bucketExist) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Bucket name exists");
        }
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        if (version) {
            minioClient.setBucketVersioning(
                    SetBucketVersioningArgs.builder()
                            .bucket(bucketName)
                            .config(new VersioningConfiguration(VersioningConfiguration.Status.ENABLED, null))
                            .build());
        }
    }

    public String getPresignUrlForGet(String bucketName, String objectName, int expireration)
            throws Exception {
        return this.minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(expireration, TimeUnit.HOURS)
                        // .extraQueryParams(param)
                        .build());

    }

    public String getPresignUrlForPut(String bucketName, String objectName, int expireration)
            throws Exception {
        return this.minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(expireration, TimeUnit.HOURS)
                        // .extraQueryParams(param)
                        .build());

    }

    public void removeFile(String bucketName, String objName) throws Exception {
        try {
            this.minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName)
                            .object(objName).build());
        } catch (ObjectNotFoundException e) {
            throw new AppException(ErrorCode.FILE_NOT_EXIST);
        }
    }

}
