package com.secure_auth.authdemo.controllers;

import com.secure_auth.authdemo.dto.request.admin.UserRoleAssignDto;
import com.secure_auth.authdemo.exception.custom.UserNotFoundExcep;
import com.secure_auth.authdemo.services.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/assign-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignRole(@RequestBody UserRoleAssignDto userRoleAssignDto) {
        try {
            adminService.assignUserRole(userRoleAssignDto);
            return ResponseEntity.status(HttpStatus.OK).body("User Role updated");
        } catch (UserNotFoundExcep e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
