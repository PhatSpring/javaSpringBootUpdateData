/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dev.com.service;

import com.dev.com.document.IdDto;
import com.dev.com.document.role;
import com.dev.com.document.userModel;
import com.dev.com.dto.AffectedRowsDto;
import com.dev.com.dto.RoleDto;
import com.dev.com.dto.UserDto;
import com.dev.com.repository.roleRepository;
import com.dev.com.repository.userRepository;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;


/**
 *
 * @author Admin
 */
@Service
public class roleService {

    @Autowired
    private roleRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Page<role> findListRole(String keyword, Pageable pageable) {

        Query query = new Query();
        query.with(pageable);
        query.with(Sort.by(Sort.Direction.DESC, "createdDate"));
        if (keyword != null) {
            Criteria criteria = new Criteria();
            criteria.orOperator(Criteria.where("role_name").regex(keyword, "i"));
            query.addCriteria(criteria);
        }
        List<role> listDocument = mongoTemplate.find(query, role.class);
        Page<role> page = PageableExecutionUtils.getPage(listDocument, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), role.class));
        return page;

    }

    public role findRoleId(ObjectId id) {
        role newRole = repository.getFindRoleId(id);

        if (newRole == null) {
            throw new DigoHttpException(11001, HttpServletResponse.SC_BAD_REQUEST);
        }
        return newRole;
    }
    //Thêm d? li?u
    public IdDto createRole(role role)
    {
        IdDto newId=new IdDto();
        role new_role=new role();
        new_role.setRole_name(role.getRole_name());
        new_role.setCreatedDate(new Date());
        new_role.setUseCount(role.getUseCount());
        role role_dto=repository.save(new_role);
        newId.setId(new_role.getId());
        return newId;

        
    }

    

    public IdDto createRole(RoleDto role) {
        IdDto newIdDto = new IdDto();
        role newRole = new role();
        newRole.setRole_name(role.getRole_name());
        newRole.setCreatedDate(new Date());
        role newroleDto = repository.save(newRole);
        newIdDto.setId(newroleDto.getId());
        return newIdDto;
    }
    //Hàm s?a d? li?u
 
    public AffectedRowsDto updateRole(ObjectId id,role role) {
     AffectedRowsDto affectedRowsDto=new AffectedRowsDto();
        try {
            role oldRole = repository.findById(id).orElse(null);
            if (Objects.nonNull(role.getRole_name())) {
                oldRole.setRole_name(role.getRole_name());
            }
            affectedRowsDto.setAffectedRows(1);
            repository.save(oldRole);
        } catch (Exception e) {
            
            System.err.println("Ki?m tra l?i có l?i!");
        }
   return affectedRowsDto;
  }
    public AffectedRowsDto deleteRoleOne(ObjectId id, role role)
    {
        AffectedRowsDto affectedRowsDto=new AffectedRowsDto();
        try {
           role old_role=repository.findById(id).orElse(null);
           if(old_role!=null && old_role.getUseCount()<1)
           {
               repository.deleteById(id);
           }
           affectedRowsDto.setAffectedRows(1);
           
        } catch (Exception e) {
        }
        return affectedRowsDto;
    }
    public AffectedRowsDto deteteRoleAll(List<ObjectId> listId)
    {
        AffectedRowsDto affectedRowsDto=new AffectedRowsDto();
        try {
            if(Objects.nonNull(listId)&& listId.size()>0)
            {
                for (ObjectId objectId : listId) {
                    role old_role=repository.findById(objectId).orElse(null);
                    if(old_role!=null)
                    {
                        repository.deleteById(objectId);
                        affectedRowsDto.setAffectedRows(1);
                    }
                    
                }
                
            }
        } catch (Exception e) {
        }
        return affectedRowsDto;
    }
    public List getRole()
{
         List<role> list_role= repository.findAll();
         return list_role;
}
    public AffectedRowsDto deleteRoleOne(ObjectId id) {
        AffectedRowsDto newAffectedRowsDto = new AffectedRowsDto();
        try {
            role oldRole = repository.findById(id).orElse(null);
            if (oldRole != null && oldRole.getUseCount() < 1) {
                repository.deleteById(id);
                newAffectedRowsDto.setAffectedRows(1);
            }
        } catch (Exception e) {
            throw new DigoHttpException(11001, HttpServletResponse.SC_NOT_FOUND);
        }
        return newAffectedRowsDto;
    }
   
}
