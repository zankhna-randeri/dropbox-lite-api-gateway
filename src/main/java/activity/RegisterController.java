package activity;

import dao.UserDao;
import model.RegisterOutput;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

  @Autowired
  private UserDao userDao;

  @PostMapping(value = "/user", consumes = {"application/json"})
  public RegisterOutput createUser(@RequestBody User user) {

    //1. check user already registered
    //2. create folder in s3
    //3. insert user in rds mysql

    if (userDao.isUserExist(user.getUserEmail())) {
      return RegisterOutput.builder().userCreated(false).build();
    } else {
      int userId = userDao.registerUser(user);
      return RegisterOutput.builder()
          .userCreated(true)
          .userId(userId)
          .build();
    }

  }
}
