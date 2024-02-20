package com.feedback.controller;

import com.feedback.payloads.user_dto.AddUserDto;
import com.feedback.payloads.user_dto.LoginDtoIn;
import com.feedback.payloads.user_dto.LoginDtoOut;
import com.feedback.payloads.user_dto.PasswordChangeDtoIn;
import com.feedback.payloads.user_dto.UserProfileDtoOut;
import com.feedback.payloads.user_dto.GetAllUsersDtoOut;
import com.feedback.service.UserService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User Controller class for managing user-related HTTP end-points.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/users")
public class UserController {

  /**
   *User Service.
   */
  @Autowired
  private UserService userService;

  /**
   * Logger initialization.
   */
  private static final Logger LOGGER = LogManager
        .getLogger(UserController.class);

  /**
   * Add new User by admin.
   *
   * @param user
   *
   * @return string if added or not.
   */
  @PostMapping("/addUser")
  public ResponseEntity<?> addUser(@Valid @RequestBody final AddUserDto user) {
    userService.saveUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body("User saved!!!");
  }

  /**
   * Change password by user.
   *
   * @param request
   *
   * @return string, whether you can change it or not.
   *
   * @throws Exception.
   *
   */
  @PostMapping("/changePassword")
  public ResponseEntity<String> changePassword(
      @Valid @RequestBody final PasswordChangeDtoIn request) throws Exception {
    String passwordChanged;
      passwordChanged = userService.passwordChangedSuccess(request);
      return ResponseEntity.ok(passwordChanged);
  }

  /**
   * login constructor.
   *
   * @param user
   *
   * @return logged in or not.
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid
        @RequestBody final LoginDtoIn user) {
    LoginDtoOut loginDtoOut = userService.getByUserAndPassword(user
      );
    LOGGER.info("Password got success");
    return ResponseEntity.status(HttpStatus.OK).body(loginDtoOut);
  }

  /**
   * Get all users to display.
   *
   *@param currentPage
   *
   * @return string.
   */
  @GetMapping("/allUsers/{currentPage}")
  public ResponseEntity<?> getAllUsers(
      @PathVariable final Integer currentPage) {
    List<GetAllUsersDtoOut> userList = userService.getAllUsers(currentPage);
    return ResponseEntity.status(HttpStatus.OK).body(userList);
  }

  /**
   * Delete a user by its' id.
   *
   * @param id
   *
   * @return string whether deleted or not.
   */
  @DeleteMapping("/deleteUser/{id}")
  public ResponseEntity<?> deleteUserById(@PathVariable final Integer id) {
    String deletedUser = userService.deleteUser(id);
    return ResponseEntity.status(HttpStatus.OK).body(deletedUser);
  }

  /**
   * Getting user by its' userName.
   *
   * @param userName
   *
   * @return userName
   */
  @GetMapping("/getByUsrName/{userName}")
  public ResponseEntity<?> getUserByUserName(
      @PathVariable final String userName) {
    UserProfileDtoOut userProfileDtoOut = userService
        .getByUserByUserName(userName);
      return ResponseEntity.status(HttpStatus.OK).body(userProfileDtoOut);
  }
}
