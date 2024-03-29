/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eui.miw.pfm.controllers.ejb;

import eui.miw.pfm.models.dao.AbstractDAOFactory;
import eui.miw.pfm.models.dao.interfaces.ProjectDAO;
import eui.miw.pfm.models.entities.ProjectEntity;
import java.util.List;

/**
 *
 * @author Roberto Amor
 */
public class ListProjectsEjb {
    
    public List<ProjectEntity> obtainProjects(final int userId){
        ProjectDAO projectDAO;
        projectDAO = AbstractDAOFactory.getFactory().getProjectDAO();
        String[] attributes = new String[1];
        attributes[0] = "user";
        String[] values = new String[1];
        values[0] = ""+userId;
        return projectDAO.find(attributes, values);
    }
}
