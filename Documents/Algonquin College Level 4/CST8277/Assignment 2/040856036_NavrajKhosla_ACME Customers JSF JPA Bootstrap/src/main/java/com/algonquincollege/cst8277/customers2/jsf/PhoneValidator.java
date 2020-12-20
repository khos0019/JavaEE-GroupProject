/*****************************************************************c******************o*******v******id********
 * File: EmailValidator.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * Student Name: Navraj Khosla
 * Student Number: 040856036
 * 
 */
package com.algonquincollege.cst8277.customers2.jsf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("phoneValidator")
public class PhoneValidator implements Validator<String>{

    // North American phoneNumber pattern
    
    /**
     * Description: Phone pattern to be followed when adding/editting a customer record.
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(\\+\\d( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$");
    
    /**
     * Description: Checks if the phone number is null or if the phone number does not follow the phone pattern. 
     * If either are true, an error message is shown to the user.
     */
    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        
        if (value == null) {
            FacesMessage msg = new FacesMessage("Phone Number should not be empty", "Invalid Phone Number format.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        
        Matcher matcher = PHONE_PATTERN.matcher(value);
        
        boolean matchFound = matcher.find();
        
        if(!matchFound) {
            FacesMessage msg = new FacesMessage("Phone should match the pattern", "Phone does not match the pattern.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        
        
    }

}