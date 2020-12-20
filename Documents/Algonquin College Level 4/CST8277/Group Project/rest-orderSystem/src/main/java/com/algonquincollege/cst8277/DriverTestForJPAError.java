/***************************************************************************f******************u************zz*******y**
 * File Name: DriverTestForJPAError
 * Course materials (20W) CST 8277
 * @author Mike Norman
 * @date 2020 04
 * 
 * Student Name: Navraj Khosla, Bakulbhai Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Description: Runs the program.
 */
public class DriverTestForJPAError {
    
    /**
     * String PU_NAME
     * 
     * Description: Holds the persistence unit name.
     */
    public static final String PU_NAME = "test-for-jpa-errors-PU";

    /**
     * Description: Main method that will run the entire project.
     * 
     * @param args
     */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
        emf.close();
    }

}
