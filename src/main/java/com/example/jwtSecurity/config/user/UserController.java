package com.example.jwtSecurity.config.user;

import com.example.jwtSecurity.BaseResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/api/auth/register")
    public BaseResponseDto registerUser(@RequestBody User newUser){
        if(userService.createUser((newUser))){
            List<String> prev2=new ArrayList<>();
            Map<Object,Object> temp=new HashMap<>();
            List<String> prev1=new ArrayList<>();
            List<String> prev3=new ArrayList<>();
            temp.put("Prev 1",prev1);
            temp.put("Prev 2",prev2);
            temp.put("Prev 3",prev3);

            return new BaseResponseDto("User Registered Successfully");
        }else{
            return new BaseResponseDto("Error");
        }
    }

    @PostMapping("/api/auth/login")
    public BaseResponseDto loginUser(@RequestBody UserLoginDto loginDetails){
        if(userService.checkuserNameExists((loginDetails.getEmail()))){
            if(userService.verifyUser(loginDetails.getEmail(),loginDetails.getPassword())){
                Optional<User> user= userService.getIndividualUser(loginDetails.getEmail());
                String token=userService.generateToken(loginDetails.getEmail(),loginDetails.getPassword());
                Map<Object,Object> temp=new HashMap<>();
                temp.put("token",token);
                temp.put("data",user);
                return new BaseResponseDto("Success",temp);
            }
            else{
                return new BaseResponseDto("Password Invalid");
            }
        }
        else{
            return new BaseResponseDto("User Not Exists");
        }
    }

    @GetMapping("/api/patient/list")
    public String hospitalLists(){
        return "List of Patients";
    }

}
