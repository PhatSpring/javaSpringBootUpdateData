/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dev.com.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Admin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {


    private String username;

    private String password;

    private String avatar;

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId role;



    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date createdDate = new Date();

    public UserDto(ObjectId id, String username, String password, String avatar, ObjectId role, List<TestDto> test) {
        
        this.username=username;
        this.password=password;
        this.avatar=avatar;
        this.role=role;
    }


}
