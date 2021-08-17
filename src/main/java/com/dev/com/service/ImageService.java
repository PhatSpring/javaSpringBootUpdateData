/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dev.com.service;

import com.cloudinary.utils.ObjectUtils;
import com.dev.com.document.cartitem;
import com.dev.com.document.image;
import com.dev.com.dto.AffectedRowsDto;
import com.dev.com.dto.ImageDto;
import com.dev.com.repository.ImageRepository;
import com.dev.com.repository.cartitemRepository;
import com.dev.com.untils.Singleton;
import java.awt.Image;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Admin
 */
@Service
public class ImageService {

    @Autowired
    private ImageRepository repository;
    @Autowired
    private MongoTemplate mongoTemplate;
    private String urls;

    public Page<image> findListImage(String keyword, Pageable pageable) {
        Query query = new Query();
        query.with(pageable);
        query.with(Sort.by(Sort.Direction.DESC, "createdDate"));
        if (keyword != null) {
            Criteria criteria = new Criteria();
            criteria.orOperator(Criteria.where("quanlity").regex(keyword, "i"));
            query.addCriteria(criteria);
        }
        List<image> listDocument = mongoTemplate.find(query, image.class);
        Page<image> page = PageableExecutionUtils.getPage(listDocument, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Image.class));
        return page;

    }

    public image uploadFile(MultipartFile file) {
        try {
            Map<image, Object> uploadResult = Singleton.getCloudinary().uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) uploadResult.get("url");
            urls=url;
            image newCloudinary = new image(url, 0);
            repository.save(newCloudinary);
            return newCloudinary;

        } catch (Exception ex) {
            return null;
        }
    }
    public image findImageId(ObjectId id)
    {
        image newImage=repository.getFindImageId(id);
          if (newImage == null) {
            throw new DigoHttpException(11001, HttpServletResponse.SC_BAD_REQUEST);
        }
        return newImage;
    }
    
    //Ham sua images
    public AffectedRowsDto PutImage(ObjectId id,MultipartFile file)
    {
        AffectedRowsDto affectedRowsDto=new AffectedRowsDto();
        
        try {
            image oldImage=repository.findById(id).orElse(null);
            Map<image, Object> uploadResult = Singleton.getCloudinary().uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) uploadResult.get("url");
              if (Objects.nonNull(url)) {
               oldImage.setUrl(url);
            }
              affectedRowsDto.setAffectedRows(1);
              repository.save(oldImage);
        } catch (Exception e) {
        }
        return affectedRowsDto;
    }
    //Ham xoa images
    public AffectedRowsDto DeleteImage(ObjectId id)
    {
        AffectedRowsDto affectedRowsDto=new AffectedRowsDto();
        try {
            image delImage=repository.findById(id).orElse(null);
            if(delImage!=null && delImage.getUseCount()<1)
            {
                repository.deleteById(id);
                affectedRowsDto.setAffectedRows(1);
            }
        } catch (Exception e) {
              throw new DigoHttpException(11001, HttpServletResponse.SC_NOT_FOUND);
        }
        return affectedRowsDto;
    }

}
