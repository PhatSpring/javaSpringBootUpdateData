/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dev.com.controller;

import com.dev.com.document.cartitem;
import com.dev.com.document.image;
import com.dev.com.dto.AffectedRowsDto;
import com.dev.com.dto.ImageDto;
import com.dev.com.service.ImageService;
import com.dev.com.service.cartitemService;
import java.awt.Image;
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
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Admin
 */
@CrossOrigin
@RestController
@RequestMapping("/Image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping
    public ResponseEntity<Object> getAllClasses(@RequestParam(required = false) String keyword, Pageable pageable) {
        Page<image> res = imageService.findListImage(keyword, pageable);
        return ResponseEntity.ok(res);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public ResponseEntity<Object> uploadImg(@RequestParam("file") MultipartFile file) {
        image newimage = imageService.uploadFile(file);
        return ResponseEntity.ok(newimage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getImageById(@PathVariable(value = "id", required = true) ObjectId id) {
        image newImage = imageService.findImageId(id);
        return ResponseEntity.ok(newImage);

    }
     @PutMapping("/{id}")
    public ResponseEntity<Object> putImage(HttpServletRequest request,
            @PathVariable(value = "id", required = true) ObjectId id,
           @RequestParam("file") MultipartFile file) {
         AffectedRowsDto idImage = imageService.PutImage(id, file);
        return ResponseEntity.ok(idImage);
    }
     @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteImageById(HttpServletRequest request,
            @PathVariable(value = "id", required = true) ObjectId id) {
        AffectedRowsDto idAffectedRowsDto = imageService.DeleteImage(id);
        return ResponseEntity.ok(idAffectedRowsDto);
    }

}
