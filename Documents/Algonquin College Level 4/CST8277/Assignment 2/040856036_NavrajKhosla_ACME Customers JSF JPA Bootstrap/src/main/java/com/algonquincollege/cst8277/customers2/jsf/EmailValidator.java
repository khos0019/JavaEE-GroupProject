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

@FacesValidator("emailValidator")
public class EmailValidator implements Validator<String>{

    /**
     * Description: Email pattern must have at least 1 letter, '@', at least 1 letter. 
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(.+)$");
    
    /**
     * Description: Checks if the email is null or if the email does not follow the email pattern. If either are true,
     * an error message is shown to the user.
     */
    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
                
        if (value == null) {
            FacesMessage msg = new FacesMessage("Email should not be empty", "Invalid Email format.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        
        Matcher matcher = EMAIL_PATTERN.matcher(value);
        
        boolean matchFound = matcher.find();
        
        if(!matchFound) {
            FacesMessage msg = new FacesMessage("Email should match the pattern", "Email does not match the pattern.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        
    }

}