package com.feedback.mapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.feedback.entities.ERole;

/**
 * ERole converter class.
 *
 * @author jagat
 *
 */
@Converter(autoApply = true)
public class EroleConverter implements AttributeConverter<ERole, String> {

  /**
   * convertToDatabaseColumn.
   * converting eRole to String.
   */
  @Override
  public String convertToDatabaseColumn(final ERole role) {
    if (role == null) {
      return null;
    }
    return role.toString();
  }

  /**
   * convertToEntityAttribute.
   * converting String to eRole.
   */
  @Override
  public ERole convertToEntityAttribute(final String role) {
    if (role == null) {
      return null;
    }
    return ERole.valueOf(role);
  }
}
