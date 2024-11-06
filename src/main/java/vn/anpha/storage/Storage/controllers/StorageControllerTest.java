package vn.anpha.storage.Storage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import vn.anpha.storage.Storage.service.StorageService;

@RestController
@RequestMapping("/storage")
public class StorageControllerTest {
    @Autowired
    private StorageService storageService;


    @PostMapping("/create-bucket/{bucket_name}")
    private ResponseEntity<?> createBucket(@PathVariable("bucket_name") String bucketName) {
        try {
            this.storageService.createBucket(bucketName,true);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        }
        catch (HttpClientErrorException e) {
                return new ResponseEntity<>(e.getStatusCode());
        }
        catch(Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }

    @GetMapping("/geturl/{bucket_name}/{object_name}")
    private ResponseEntity<?> getUrl(@PathVariable("bucket_name") String bucketName, @PathVariable("object_name") String objectName) {
        try {
            String res = this.storageService.getPresignUrl(bucketName, objectName, 1);
            System.err.println(res);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        }
        catch (HttpClientErrorException e) {
                return new ResponseEntity<>(e.getStatusCode());
        }
        catch(Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }
}
