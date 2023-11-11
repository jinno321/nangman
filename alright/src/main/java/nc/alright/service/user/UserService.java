package nc.alright.service.user;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import nc.alright.domain.user.JoinStatus;
import nc.alright.domain.user.LoginStatus;
import nc.alright.domain.user.User;
import nc.alright.repository.user.JpaUserRepository;
import nc.alright.repository.user.UserRepository;
import nc.alright.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//@Service
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey;
    private Long expiredMs = 1000 * 60 * 60l;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //로그인 시 예외 던짐

    public JoinStatus join(User user){

        try{
            validateDuplicateMember(user);
        }
        catch (IllegalStateException e){
            System.out.println(e);
            return JoinStatus.DUPLICATE;
        }
        try{
            checkPasswordLength(user);
        }
        catch (IllegalStateException e){
            System.out.println(e);
            return JoinStatus.INVALID_PASSWORD_LENGTH;
        }
        try{
            checkStrongPassword(user);
        }
        catch (IllegalStateException e)
        {
            System.out.println(e);
            return JoinStatus.INVALID_PASSWORD_STRENGTH;
        }

        userRepository.save(user);
        return JoinStatus.SUCCESS;
    }
    //////////jwt
    public String afterSuccessLogin(String userEmail) {
        //return JwtUtil.createJwt(userEmail, "normal", secretKey, expiredMs);
        return JwtUtil.createJwt(userEmail, secretKey, expiredMs);
    }

    public User getUserInfoByJWT(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String jwtToken = "";

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
        }

        String userEmail = JwtUtil.getUserName(jwtToken, secretKey);
        Optional<User> ret = userRepository.findByEmail(userEmail);
        return ret.orElse(null);
    }


    //////////////jwt
    public LoginStatus login(String userEmail, String userPassword) {
        User user = userRepository.getUserByEmail(userEmail);
        log.info(userEmail);
        log.info(user.getUserName());
        log.info(user.getUserEmail());
        log.info(user.getPassword());
        log.info(userPassword);
        if (user == null || !user.getPassword().equals(userPassword)) {
            return LoginStatus.FAIL;
        }
        return LoginStatus.SUCCESS;
    }

/*
    private void validateDuplicateMember(User user) {
        userRepository.findByEmail(user.getUserEmail()).ifPresent(existingUser -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
    }

 */
    private void validateDuplicateMember(User user) {
        // 이미 존재하는 사용자인지 확인
        Optional<User> existingUser = userRepository.findByEmail(user.getUserEmail());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }



    private void checkPasswordLength(User user) {
        String password = user.getPassword();
        if (password.length() < 8 || password.length() > 16) {
            throw new IllegalStateException("비밀번호는 8 ~ 16자 사이여야 합니다.");
        }
    }

    private void checkStrongPassword(User user) {
        String password = user.getPassword();
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if ("!@#$%^&*()-_=+[]{}|;:'\",.<>/?".indexOf(c) != -1) {
                hasSpecialChar = true;
            }
        }

        if (!(hasUpperCase && hasLowerCase && hasSpecialChar)) {
            throw new IllegalStateException("비밀번호는 영문 소문자, 대문자, 특수문자를 포함해야됩니다.");
        }
    }

    public User getUserInfoById(Long userId) {
        return userRepository.getUserById(userId);
    }

    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    public User getUserById(Long userId) {
        return userRepository.getUserById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User updateUser(Long userId, User updatedUser) {
        return userRepository.updateUser(userId, updatedUser);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }

    public User getUserByEmail(String userEmail) {
        return userRepository.getUserByEmail(userEmail);
    }

    public User getUserByPhoneNumber(String userPhoneNumber) {
        return userRepository.getUserByPhoneNumber(userPhoneNumber);
    }

    public List<User> getUsersByUserName(String userName) {
        return userRepository.getUsersByUserName(userName);
    }


}
