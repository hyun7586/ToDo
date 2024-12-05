package org.example.todo.service.user;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.todo.domain.UserEntity;
import org.example.todo.dto.mapper.UserMapper;
import org.example.todo.dto.user.UserAddRequest;
import org.example.todo.dto.user.UserResponse;
import org.example.todo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final UserMapper usermapper;

  // find all
  public List<UserResponse> findAll(){
    List<UserEntity> temp = userRepository.findAll();

    return temp.stream()
        .map((each) -> UserResponse.builder()
            .userId(each.getUserId())
            .userName(each.getUsername())
            .userEmail(each.getUserEmail())
            .build()).toList();
  }

  // find the user by id and return UserResponse
  public UserResponse findById(Long id){
    UserEntity target =  userRepository.findById(id).orElse(null);

    if(target==null){
      return null;
    }else{
      return usermapper.toResponse(target);
    }
  }

  // find the user by id and return UserEntity
  public UserEntity findByIdAndReturnUserEntity(Long id){
    return userRepository.findById(id).orElse(null);
  }

  public UserEntity findByEmailAndReturnUserEntity(String email){
    return userRepository.findByUserEmail(email).orElse(null);
  }

  // save the user
  public Long save(UserAddRequest request){
    return userRepository.save(UserEntity.builder()
        .userEmail(request.getUserEmail())
        .userPassword(bCryptPasswordEncoder.encode(request.getUserPassword()))
        .userName(request.getUserName())
        .build()).getUserId();
  }

  // delete the user
  public void deleteById(Long id){
    userRepository.deleteById(id);
    return;
  }


}
