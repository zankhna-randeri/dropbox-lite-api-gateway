package activity;

import model.RegisterOutput;
import model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.S3service;
import service.UserService;

@RestController
public class RegisterController {

  @PostMapping(value = "/user", consumes = {"application/json"})
  public RegisterOutput createUser(@RequestBody User user) {

    //1. check user already registered
    //2. create folder in s3
    //3. insert user in rds mysql

    UserService userService = new UserService();
    if (userService.isUserExist(user.getUserEmail())) {
      return RegisterOutput.builder().userCreated(false).build();
    } else {
      int userId = userService.insertUser(user);
      S3service s3Service = new S3service();
      s3Service.createFolder(Integer.toString(userId));
      return RegisterOutput.builder()
          .userCreated(true)
          .userId(userId)
          .build();
    }

  }
}
