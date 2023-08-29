package com.green.babymeal.user;

import com.green.babymeal.auth.AuthService;
import com.green.babymeal.common.entity.UserEntity;
import com.green.babymeal.user.model.UserDelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository rep;
    private final UserMapper mapper;
    private final AuthService service;

    /*public ResponseEntity<UserEntity> insUser(UserInsDto dto){
        UserEntity entity = new UserEntity();
        entity.setName(dto.getName());
        entity.setNickNm(dto.getNick_name());
        entity.setAddress(dto.getAddress());
        entity.setAddressDetail(dto.getAddress_detail());
        entity.setBirthday(dto.getBirthday());
        entity.setEmail(dto.getEmail());
        entity.setZipCode(dto.getZip_code());
        entity.setMobile_nb(dto.getMobile_nb());
        entity.setPassword(dto.getPassword());
        entity.setUid(dto.getUid());
       return ResponseEntity.ok(rep.save(entity));
    }*/

    public UserEntity selUser(String uid){
        UserEntity entity = new UserEntity();
        entity.setUid(uid);
        UserEntity userEntity=rep.findByUid(uid);
        return userEntity;
    }

    public List<UserEntity> selUserAll(){
        return rep.findAll();
    }

    public void delUser(String email){

    }





}
