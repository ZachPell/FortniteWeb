package enhanced;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebappListener implements ServletContextListener {  
    public void contextInitialized(ServletContextEvent sce)
    {}

    public void contextDestroyed(ServletContextEvent sce)
    {
        try {
            Enumeration<Driver> enumer = DriverManager.getDrivers();
            while (enumer.hasMoreElements()) {
                DriverManager.deregisterDriver(enumer.nextElement());
            }
        } catch (java.sql.SQLException se) {
            se.printStackTrace();
        }   
        HibernateHelper.closeFactory();
  }
} 
