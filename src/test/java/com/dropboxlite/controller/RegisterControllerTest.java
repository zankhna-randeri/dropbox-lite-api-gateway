package com.dropboxlite.controller;

import com.dropboxlite.dao.UserDao;
import com.dropboxlite.exception.InvalidRequestException;
import com.dropboxlite.model.RegisterOutput;
import com.dropboxlite.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class RegisterControllerTest {

  @InjectMocks
  private RegisterController controller;

  @Mock
  private UserDao userDao;

  @Test
  public void registerUserAlreadyExistTest() {
    //setup
    Mockito.when(userDao.isUserExist("abc@gmail.com")).thenReturn(true);
    User user = User.builder().userEmail("abc@gmail.com")
        .firstName("abc")
        .lastName("xyz")
        .password("xxxxx").build();

    //call
    RegisterOutput output = controller.createUser(user);

    //verify
    Assert.assertFalse(output.isUserCreated());
  }

  @Test
  public void registerUserAlreadyNotExistTest() {
    //setup
    Mockito.when(userDao.isUserExist("xyz@gmail.com")).thenReturn(false);
    User user = User.builder().userEmail("xyz@gmail.com")
        .firstName("abc")
        .lastName("xyz")
        .password("xxxxx").build();

    //call
    RegisterOutput output = controller.createUser(user);

    //verify
    Assert.assertTrue(output.isUserCreated());
  }

  @Test(expected = InvalidRequestException.class)
  public void invalidUserRequestTest() {
    controller.createUser(null);
  }

  @Test(expected = InvalidRequestException.class)
  public void emptyUserEmailTest() {
    //setup
    User user = User.builder()
        .firstName("abc").userEmail("")
        .lastName("xyz")
        .password("xxxxx").build();
    controller.createUser(user);
  }

  @Test(expected = InvalidRequestException.class)
  public void nullUserEmailTest() {
    //setup
    User user = User.builder()
        .firstName("abc")
        .lastName("xyz")
        .password("xxxxx").build();
    controller.createUser(user);
  }

  @Test
  public void registerUserSuccessTest() {
    //setup
    Mockito.when(userDao.isUserExist("abc@gmail.com")).thenReturn(false);
    User user = User.builder()
        .firstName("abc").userEmail("abc@gmail.com")
        .lastName("xyz")
        .password("xxxxx").build();
    RegisterOutput expected = RegisterOutput.builder()
        .userCreated(true).build();

    //call
    RegisterOutput output = controller.createUser(user);

    //verify
    Assert.assertEquals(expected, output);
  }
}
