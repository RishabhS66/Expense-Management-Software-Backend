package com.adobe.prj.util;

import javax.persistence.Converter;

import com.adobe.prj.entity.Address;

/**
 * Concrete converter class which extends the
 * {@link GenericJsonAttributeConverter} class. <br/>
 * Note: No need to do anything here. This is used to pass the attribute type to
 * the GenericJsonAttributeConverter
 * 
 * @author Sunit Katkar, sunitkatkar@gmail.com
 *         (https://sunitkatkar.blogspot.com/)
 * @since ver 1.0 (May 2018)
 * @version 1.0
 */
@Converter(autoApply = false)
public class AddressJsonConverter extends GenericJsonAttributeConverter<Address> {

}
