package com.green.babymeal.user;

import com.green.babymeal.auth.AuthService;
import com.green.babymeal.common.entity.UserEntity;
import com.green.babymeal.user.model.UserDelDto;
import com.green.babymeal.user.model.UserSelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    public UserSelVo selUserAll(Pageable pageable){
        Page<UserEntity> all = rep.findAll(pageable);
        List<UserEntity> list = all.get().toList();
        UserSelVo vo = new UserSelVo();
        vo.setPage(pageable.getPageNumber());
        vo.setMaxPage(all.getTotalPages());
        vo.setCount(all.getSize());
        vo.setList(list);
        return vo;
    }

    public void delUser(String uid){
        UserEntity opt = rep.findByUid(uid);
        opt.setDelYn((byte)1);
        rep.save(opt);
    }





}
