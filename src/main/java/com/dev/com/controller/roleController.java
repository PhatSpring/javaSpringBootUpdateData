/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dev.com.controller;

import com.dev.com.document.IdDto;
import com.dev.com.document.role;
import com.dev.com.dto.AffectedRowsDto;
import com.dev.com.dto.RoleDto;
import com.dev.com.dto.UserDto;
import com.dev.com.service.roleService;
import com.dev.com.service.userService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@CrossOrigin
@RestController
@RequestMapping("/role")
public class roleController {

    @Autowired
    private roleService roleService;

    @GetMapping
    public ResponseEntity<Object> getAllClasses(@RequestParam(required = false) String keyword, Pageable pageable) {
        Page<role> res = roleService.findListRole(keyword, pageable);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRoleById(@PathVariable(value = "id", required = true) ObjectId id) {
        role newRole = roleService.findRoleId(id);
        return ResponseEntity.ok(newRole);

    }

    @PostMapping
    public ResponseEntity<Object> postRole(HttpServletRequest request,
            @RequestBody RoleDto role) {
        IdDto idRole = roleService.createRole(role);
        return ResponseEntity.ok(idRole);
    }
   
    @PutMapping("/{id}")
    public ResponseEntity<Object> putRole(HttpServletRequest request,
            @PathVariable(value = "id", required = true) ObjectId id,
            @RequestBody role role) {
        AffectedRowsDto idRole = roleService.updateRole(id, role);
      //  System.out.println("K?t qu? idRole:"+idRole);
        return ResponseEntity.ok(idRole);
    }
    @GetMapping("/all")
public List getRoles() {
    return roleService.getRole();
}
@DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOne(HttpServletRequest request,
            @PathVariable(value = "id", required = true) ObjectId id) {
        AffectedRowsDto idAffectedRowsDto = roleService.deleteRoleOne(id);
        return ResponseEntity.ok(idAffectedRowsDto);
    }
    

}
