package com.bookmyconsultation.paymentservice.security;


import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum ApplicationRole {

    USER(Sets.newHashSet(ApplicationPermission.DOCTOR_REGISTRATION, ApplicationPermission.DOCTORS_LIST_VIEW,
            ApplicationPermission.DOCTOR_VIEW,ApplicationPermission.DOCTOR_DOCUMENTS_DOWNLOAD,
            ApplicationPermission.DOCTOR_DOCUMENTS_UPLOADED_VIEW, ApplicationPermission.DOCTOR_DOCUMENTS_UPLOAD,
            ApplicationPermission.USER_REGISTRATION,ApplicationPermission.USER_DETAILS_VIEW,
            ApplicationPermission.USER_DOCUMENTS_UPLOAD, ApplicationPermission.USER_DOCUMENTS_DOWNLOAD,
            ApplicationPermission.USER_DOCUMENTS_UPLOADED_VIEW,
            ApplicationPermission.DOCTOR_AVAILABILITY_UPDATE,ApplicationPermission.DOCTOR_AVAILABILITY_VIEW,
            ApplicationPermission.BOOK_DOCTOR_APPOINTMENT, ApplicationPermission.APPOINTMENT_DETAILS_VIEW,
            ApplicationPermission.USER_APPOINTMENTS_VIEW, ApplicationPermission.PRESCRIPTION_UPLOAD,
            ApplicationPermission.PAYMENT_FOR_APPOINTMENT, ApplicationPermission.RATE_DOCTOR)),

    ADMIN(Sets.newHashSet(ApplicationPermission.DOCTOR_REGISTRATION, ApplicationPermission.DOCTOR_APPROVAL,
            ApplicationPermission.DOCTOR_REJECTION, ApplicationPermission.DOCTORS_LIST_VIEW,
            ApplicationPermission.DOCTOR_VIEW,ApplicationPermission.DOCTOR_DOCUMENTS_DOWNLOAD,
            ApplicationPermission.DOCTOR_DOCUMENTS_UPLOADED_VIEW, ApplicationPermission.DOCTOR_DOCUMENTS_UPLOAD,
            ApplicationPermission.USER_DETAILS_VIEW, ApplicationPermission.USER_DOCUMENTS_DOWNLOAD,
            ApplicationPermission.USER_DOCUMENTS_UPLOADED_VIEW, ApplicationPermission.USER_DOCUMENTS_UPLOAD,
            ApplicationPermission.DOCTOR_AVAILABILITY_UPDATE,ApplicationPermission.DOCTOR_AVAILABILITY_VIEW,
            ApplicationPermission.BOOK_DOCTOR_APPOINTMENT, ApplicationPermission.APPOINTMENT_DETAILS_VIEW,
            ApplicationPermission.USER_APPOINTMENTS_VIEW));

    private Set<ApplicationPermission> permissions;

    ApplicationRole(Set<ApplicationPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationPermission> getPermissions() {
        return this.getPermissions();
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = permissions.stream().map(
                        p -> new SimpleGrantedAuthority(p.name()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
