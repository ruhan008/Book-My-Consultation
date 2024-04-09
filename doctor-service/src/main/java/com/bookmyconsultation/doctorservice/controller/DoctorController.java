package com.bookmyconsultation.doctorservice.controller;

import com.bookmyconsultation.doctorservice.model.Doctor;
import com.bookmyconsultation.doctorservice.model.request.DoctorDetailsRequest;
import com.bookmyconsultation.doctorservice.repository.S3Repository;
import com.bookmyconsultation.doctorservice.service.DoctorService;
import com.bookmyconsultation.doctorservice.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import freemarker.template.TemplateException;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private S3Repository s3Repository;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/doctors")
    public ResponseEntity<Doctor> registerDoctor(@RequestBody @Valid Doctor doctor) throws IOException,
            TemplateException, MessagingException {
        return new ResponseEntity<>(doctorService.registerDoctor(doctor), HttpStatus.OK);
    }

    /**
     * This endpoint has been added for admins to verify their email so that they can
     * set it as the default from-email in notification-service
     *
     * @param emailId emailId
     * @return String acknowledging the process
     */
    @GetMapping("/verify-email")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String emailId){
        emailService.verifyEmail(emailId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/doctors/{doctorId}/document")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> uploadDocuments(@PathVariable String doctorId,
                                                  @RequestParam MultipartFile[] files) throws IOException {
        for(MultipartFile file : files) {
            s3Repository.uploadFiles(doctorId,file);
        }
        return new ResponseEntity<>("File(s) uploaded successfully",HttpStatus.OK);
    }

    @PutMapping("/doctors/{doctorId}/approve")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Doctor> approveDoctorsRegistration(@PathVariable String doctorId, @RequestBody Doctor doctor)
            throws JsonProcessingException {
        return new ResponseEntity<>(doctorService.approveDoctorsRegistration(doctorId,doctor),HttpStatus.OK);
    }

    @PutMapping("/doctors/{doctorId}/reject")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Doctor> rejectDoctorsRegistration(@PathVariable String doctorId, @RequestBody Doctor doctor)
            throws JsonProcessingException {
        return new ResponseEntity<>(doctorService.rejectDoctorsRegistration(doctorId,doctor),HttpStatus.OK);
    }

    @GetMapping("/doctors")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Doctor>> getDoctorsBasedOnFilterCriteria(@RequestParam(name = "status") String status,
                                                                        @RequestParam(name = "speciality",required = false)
                                                                        String speciality) {
        return new ResponseEntity<>(doctorService.getDoctorsBasedOnFilterCriteria(status, speciality), HttpStatus.OK);
    }

    @GetMapping("/doctors/{doctorId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Doctor> getDoctorDetails(@PathVariable(name = "doctorId") String doctorId) {
        return new ResponseEntity<>(doctorService.getDoctorDetails(doctorId),HttpStatus.OK);
    }

    @GetMapping("/doctors/{doctorId}/documents/metadata")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<String>> getDoctorUploadedDocumentsData(@PathVariable String doctorId){
        return new ResponseEntity<>(s3Repository.listBucketObjects(doctorId), HttpStatus.OK);
    }

    @GetMapping("/doctors/{doctorId}/documents/{documentName}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<byte[]> getDoctorUploadedFile (@PathVariable(name = "doctorId") String doctorId,
                                                                @PathVariable(name = "documentName") String
                                                                documentName) throws IOException {
        return ResponseEntity.ok()
                .contentType(contentType(documentName))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + documentName + "\"")
                .body(s3Repository.getDoctorUploadedFile(doctorId, documentName).toByteArray());
    }

    @GetMapping("/doctor/{doctorId}/details")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public DoctorDetailsRequest getDoctorDetailsRequest (@PathVariable(name = "doctorId")
                                                                                     String doctorId) {
        return doctorService.getDoctorDetailsRequest(doctorId);
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
