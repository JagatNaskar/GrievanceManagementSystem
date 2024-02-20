package com.feedback.payloads.user_dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PasswordChangeDtoinTest {

  @Test
  public void testGettersAndSetters() {
    PasswordChangeDtoIn passwordChangeDTO = new PasswordChangeDtoIn();
    passwordChangeDTO.setUserName("Jagat Naskar");
    passwordChangeDTO.setOldPassword("oldPasswordNucleusteq");
    passwordChangeDTO.setNewPassword("newPassword");
    passwordChangeDTO.setConfirmNewPassword("newPassword");

    assertEquals("Jagat Naskar", passwordChangeDTO.getUserName());
    assertEquals("oldPasswordNucleusteq", passwordChangeDTO.getOldPassword());
    assertEquals("newPassword", passwordChangeDTO.getNewPassword());
    assertEquals("newPassword", passwordChangeDTO.getConfirmNewPassword());
  }

  @Test
  public void testEqualsAndHashCode() {
    PasswordChangeDtoIn passwordChangeDTO1 = new PasswordChangeDtoIn("Jagat Naskar", 
        "oldPasswordNucleusteq", 
        "newPassword", 
        "newPassword");
    PasswordChangeDtoIn passwordChangeDTO2 = new PasswordChangeDtoIn("Jagat Naskar", 
        "oldPasswordNucleusteq", 
        "newPassword", 
        "newPassword");

    assertTrue(passwordChangeDTO1.equals(passwordChangeDTO2));
    assertTrue(passwordChangeDTO2.equals(passwordChangeDTO1));
    assertEquals(passwordChangeDTO1.hashCode(), passwordChangeDTO2.hashCode());

    passwordChangeDTO2.setConfirmNewPassword("differentPassword");

    assertFalse(passwordChangeDTO1.equals(passwordChangeDTO2));
    assertFalse(passwordChangeDTO2.equals(passwordChangeDTO1));
    assertNotEquals(passwordChangeDTO1.hashCode(), passwordChangeDTO2.hashCode());
  }

  @Test
  public void testConstructor() {
    PasswordChangeDtoIn passwordChangeDTO = new PasswordChangeDtoIn(
      "Jagat Naskar", 
      "oldPasswordNucleusteq", 
      "newPassword", 
      "newPassword");

    assertEquals("Jagat Naskar", passwordChangeDTO.getUserName());
    assertEquals("oldPasswordNucleusteq", passwordChangeDTO.getOldPassword());
    assertEquals("newPassword", passwordChangeDTO.getNewPassword());
    assertEquals("newPassword", passwordChangeDTO.getConfirmNewPassword());
  }

  @Test
  public void testFieldConstructor() {
    PasswordChangeDtoIn passwordChangeDTO = new PasswordChangeDtoIn(
        "Jagat Naskar", 
        "oldPasswordNucleusteq", 
        "newPassword", 
        "newPassword");

    assertEquals("Jagat Naskar", passwordChangeDTO.getUserName());
    assertEquals("oldPasswordNucleusteq", passwordChangeDTO.getOldPassword());
    assertEquals("newPassword", passwordChangeDTO.getNewPassword());
    assertEquals("newPassword", passwordChangeDTO.getConfirmNewPassword());
  }
  
  
  
  
  
  
  
  
  
  
  
  

  @Test
  void testEquals() {
    PasswordChangeDtoIn dto1 = new PasswordChangeDtoIn("user", "old", "new", "new");
    PasswordChangeDtoIn dto2 = new PasswordChangeDtoIn("user", "old", "new", "new");
    PasswordChangeDtoIn dto3 = new PasswordChangeDtoIn("user", "old", "new", "different");

    assertTrue(dto1.equals(dto2));
    assertTrue(dto2.equals(dto1));
    assertEquals(dto1.hashCode(), dto2.hashCode());

    assertFalse(dto1.equals(dto3));
    assertFalse(dto2.equals(dto3));
  }

  @Test
  void testToString() {
    PasswordChangeDtoIn dto = new PasswordChangeDtoIn("user", "old", "new", "new");
    String expectedString = "PasswordChangeDTOin [oldPassword=old, newPassword=new, confirmNewPassword=new]";
    assertEquals(expectedString, dto.toString());
  }

}
