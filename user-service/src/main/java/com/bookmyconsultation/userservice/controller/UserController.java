package com.bookmyconsultation.userservice.controller;

import com.bookmyconsultation.userservice.model.User;
import com.bookmyconsultation.userservice.model.request.UserDetailsRequest;
import com.bookmyconsultation.userservice.repository.S3Repository;
import com.bookmyconsultation.userservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private S3Repository s3Repository;

    @PostMapping("/users")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<User> registerNewUser(@RequestBody @Valid User user) throws JsonProcessingException {
        return new ResponseEntity<>(userService.registerNewUser(user), HttpStatus.OK);
    }

    @GetMapping("/users/{userID}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<User> getUserDetails(@PathVariable(name = "userID") String userId) {
        return new ResponseEntity<>(userService.getUserDetails(userId), HttpStatus.OK);
    }

    @PostMapping("/users/{id}/documents")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> uploadUserDocuments(@PathVariable String id, @RequestBody MultipartFile[] files)
            throws IOException {
        for (MultipartFile multipartFile : files) {
            s3Repository.uploadFiles(id, multipartFile);
        }
        return new ResponseEntity<>("File(s) uploaded successfully.", HttpStatus.OK);
    }

    @GetMapping("/users/{id}/documents/metadata")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<String>> getUserUploadedDocumentsData(@PathVariable String id){
        return new ResponseEntity<>(s3Repository.listBucketObjects(id), HttpStatus.OK);
    }

    @GetMapping("/users/{id}/documents/{documentName}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<byte[]> getUserUploadedFile (@PathVariable(name = "id") String id,
                                                         @PathVariable(name = "documentName") String
                                                                 documentName) throws IOException {
        return ResponseEntity.ok()
                .contentType(contentType(documentName))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + documentName + "\"")
                .body(s3Repository.getUserUploadedFile(id, documentName).toByteArray());
    }

    @GetMapping("users/{userId}/details")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public UserDetailsRequest getUserDetailsRequest (@PathVariable String userId) {
        return userService.getUserDetailsRequest(userId);
    }

    private MediaType contentType(String filename) {
        String[] fileArrSplit = filename.split("\\.");
        String fileExtension = fileArrSplit[fileArrSplit.length - 1];
        switch (fileExtension) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            case "pdf":
                return MediaType.APPLICATION_PDF;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
