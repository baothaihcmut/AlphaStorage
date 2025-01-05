package vn.anpha.storage.Storage.service;

import java.util.concurrent.TimeUnit;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.SetBucketVersioningArgs;
import io.minio.http.Method;
import io.minio.messages.Item;
import io.minio.messages.VersioningConfiguration;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Storage.DTO.VersionLinkDTO;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final MinioClient minioClient;

    @Value("${minio.host}")
    private String MINIO_HOST;

    @Value("${minio.url}")
    private String MINIO_ENDPOINT;

    @Value("${env}")
    private String APP_ENV;

    private String replaceHost(String presignUrl) {
        return presignUrl.replaceAll(MINIO_ENDPOINT, MINIO_HOST);
    }

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

    public String getPresignUrlForUpdate(String bucketName, String objectName, int duration) throws Exception {
        String url = this.minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(duration, TimeUnit.HOURS)
                        // .extraQueryParams(param)
                        .build());
        return APP_ENV.equals("dev") ? url : this.replaceHost(url);
    }

    public String getPresignUrlForPut(String bucketName, String objectName, int expireration)
            throws Exception {
        String url = this.minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(expireration, TimeUnit.HOURS)
                        // .extraQueryParams(param)
                        .build());
        return APP_ENV.equals("dev") ? url : this.replaceHost(url);
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

    public VersionLinkDTO getLastVersion(String bucket, String object) {
        Iterable<Result<Item>> items = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .includeVersions(true)
                        .bucket(bucket).prefix(object)
                        .build());
        for (Result<Item> item : items) {
            try {
                Item version = item.get();
                if (version.isLatest()) {
                    return new VersionLinkDTO(version.versionId(), Long.valueOf(version.size()).intValue(),
                            version.lastModified().toLocalDateTime());
                }
            } catch (Exception e) {
                throw new AppException(ErrorCode.SERVER_ERROR);
            }
        }
        throw new AppException(ErrorCode.FILE_NOT_UPLOAD);
    }

    public void removeVersionOfFile(String bucket, String object, String versionId) {
        try {
            this.minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .versionId(versionId)
                            .object(object).build());
        } catch (Exception e) {
            throw new AppException(ErrorCode.SERVER_ERROR);
        }
    }

}
