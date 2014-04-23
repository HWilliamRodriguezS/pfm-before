
package eui.miw.pfm.controllers.beans;

import java.util.Date;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;


public class TestConfProjectBean {
    
    private ConfProjectBean confProjectBean;
    
    @Before
    public void init(){
        this.confProjectBean = new ConfProjectBean();
    }
    
    
    @Test
    public void testValidDates(){
        
        Date[][] dates = new Date[3][2];
        
        dates[0][0] = new Date(System.currentTimeMillis());
        dates[0][1] = new Date(System.currentTimeMillis() + (1 * 30 * 24 * 60 * 1000 ) );
        
        dates[1][0] = new Date(System.currentTimeMillis());
        dates[1][1] = new Date(System.currentTimeMillis() + (1 * 30 * 24 * 60 * 1000 ) );
        
        dates[2][0] = new Date(System.currentTimeMillis());
        dates[2][1] = new Date(System.currentTimeMillis() + (1 * 30 * 24 * 60 * 1000 ) );
        
        for(int i = 0 ; i< dates.length ; i++){
            
            validDates(dates[i][0],dates[i][1]);
            
        }
        
    }
    
    public void validDates(Date start,Date end){
        
        assertTrue("Valid Dates",confProjectBean.validDates(start, end));
    
    }
    
}
