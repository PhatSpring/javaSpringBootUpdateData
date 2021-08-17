/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dev.com.service;

import com.dev.com.document.IdDto;
import com.dev.com.document.userModel;
import com.dev.com.dto.RoleDto;
import com.dev.com.dto.UserDto;
import com.dev.com.repository.userRepository;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author huunghia
 */
@Service
public class userService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private userRepository repository;

    public Page<userModel> findList(Pageable pageable) {
        //thuoc lop org.springframework.data.mongodb.core.query
        Query query = new Query();
        query.with(pageable);
//        Pageable
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
        System.out.println(query);

        List<userModel> users = repository.findAll();
        System.out.println(users);

        List<userModel> listDocument = mongoTemplate.find(query, userModel.class);
        System.out.println(listDocument);
        Page<userModel> page = PageableExecutionUtils.getPage(listDocument, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), userModel.class));
        return page;

    }

    public UserDto findUserId(ObjectId id) {
        userModel newUserModel = repository.getFindUserId(id);
        UserDto newUserDto = new UserDto();

        if (newUserModel == null) {
            throw new DigoHttpException(11001, HttpServletResponse.SC_BAD_REQUEST);
        }
        return newUserDto;
    }

    public IdDto createUser(UserDto userDto) {
        IdDto idnew = new IdDto();
        String password = new BCryptPasswordEncoder().encode(userDto.getPassword());
        userModel newUser = new userModel();
        if (Objects.nonNull(userDto.getUsername())) {
            newUser.setUsername(userDto.getUsername());
        }
        if (Objects.nonNull(userDto.getAvatar())) {
            newUser.setAvatar(userDto.getAvatar());
        }
        if (Objects.nonNull(userDto.getRole())) {
            newUser.setRole(userDto.getRole());
        }
        if (Objects.nonNull(password)) {
            newUser.setPassword(password);
        }
        System.out.println("user" + newUser);
        userModel userId = repository.save(newUser);
        idnew.setId(userId.getId());

        return idnew;
    }
    //Hàm s?a d? li?u

}
