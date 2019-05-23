package enhanced;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.criterion.Restrictions;

public class HibernateHelper {
    
    static protected Logger log =
      Logger.getLogger("error.log");
    static protected List<Class> listClasses = new ArrayList<Class>();
    static protected SessionFactory sessionFactory;
    static protected Exception lastError;
    
    static
    public void initSessionFactory(Properties props, Class... mappings) 
    {
        if (addMappings(listClasses, mappings)) {
            closeFactory(sessionFactory);
            sessionFactory = createFactory(props, listClasses);
        }
    }
    
    static public void initSessionFactory(Class... mappings) {
        initSessionFactory(null, mappings);
    }
    
    static protected void testConnection(SessionFactory factory)
            throws Exception {
        Session session = null;
        try {
            session = factory.openSession();    
            Transaction tran = session.beginTransaction();      
            tran.commit();
            session.close();
        } catch (Exception ex) {
            log.error("Database transaction failed.", ex);
            if (session != null) session.close();
            throw ex;
        }
    }
        
    static public boolean isSessionOpen() {
        return sessionFactory != null;
    }
    
    static public boolean testDB(HttpServletResponse response)
            throws IOException, ServletException {
        if (!isSessionOpen()) {
            writeError(response);
        }
        return isSessionOpen();
    }
    
    static public void writeError(HttpServletResponse response, String title, Exception ex)
              throws java.io.IOException, ServletException
    {
        java.io.PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("<html>");
        out.println("  <head>");
        out.println("    <title>" + title + "</title>");
        out.println("  </head>");
        out.println("  <body>");
        out.println("<h2>" + title + "</h2>");
        if (ex != null) {
            if (ex.getMessage() != null) {
                out.println(
                "    <h3>" + ex.getMessage() + "</h3>");
            }
            if (ex.getCause() != null) {
                out.println(
                "    <h4>" + ex.getCause() + "</h4>");
            }
            StackTraceElement[] trace = ex.getStackTrace();
            if (trace != null && trace.length > 0) {
                out.print("<pre>");
                ex.printStackTrace(out);
                out.println("</pre>");
            }
        } else {
            out.println("Hibernate must be initialized");
        }
        out.println("  </body>");
        out.println("</html>");
        out.close();
    }
    
    static public void writeError(HttpServletResponse response)
        throws java.io.IOException, ServletException {
            writeError(response,"Hibernate Initialization Error",lastError);
    }
    
    static protected boolean addMappings(List<Class> list, 
                                Class... mappings) 
    {
        boolean bNewClass = false;
        for (Class mapping : mappings) {
            if (!list.contains(mapping)) {
                list.add(mapping);
                bNewClass = true;
            }
        }
        return bNewClass;
    }
    
    static public java.util.List getListData(
      Class classBean, String strKey, Object value)
    {
        java.util.List result = new java.util.ArrayList();
    
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
    
        Criteria criteria = session.createCriteria(classBean);
    
        if (strKey != null) {
            criteria.add(Restrictions.like(strKey, value));
        }
        
        result = criteria.list();
    
        tx.commit();
        session.close();
        return result;
    }
    
    static public java.util.List getListData(
      Class classBean) {
        return getListData(classBean, null, null);
    }
    
    static public void updateDB(Object obj) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
      
            session.saveOrUpdate(obj);
      
            tx.commit();
        } finally {
            if (session != null) session.close();
        }
    }
    
    static public void updateDB(java.util.List list) {
    
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
    
        for(Object obj : list) {
            session.saveOrUpdate(obj);
        }
    
        tx.commit();
        session.close();
    } 
    
    static public void createTable(Properties props, 
                          Class... mappings) 
    {
        List<Class> tempList = new ArrayList<Class>();
        
        SessionFactory tempFactory = null;
    
        addMappings(tempList, mappings);
        
        if (props == null) props = new Properties();
            props.setProperty(Environment.HBM2DDL_AUTO, "create");

        tempFactory = createFactory(props, tempList);
        closeFactory(tempFactory);
    }
    
    static public void createTable(Class... mappings) {
        createTable(null, mappings);
    }
    
    static protected SessionFactory createFactory( 
                               Properties props,
                               List<Class> list) {
        SessionFactory factory = null;
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        try {
            if (props != null) cfg.addProperties(props);

            for (Class mapping : list) {
                cfg.addAnnotatedClass(mapping);
            }
            factory = buildFactory(cfg);
            testConnection(factory);
        } catch (Exception ex) {
            lastError = ex;
            closeFactory(factory);
            factory = null;
        }
        return factory;
    }
    
    static protected void configureFromFile(Configuration cfg)
                throws Exception {
        try {
            cfg.configure();
        } catch (HibernateException ex) {
            if (ex.getMessage().equals("/hibernate.cfg.xml not found")) {
                log.warn(ex.getMessage());
            } else {
                log.error("Error in hibernate " + "configuration file.", ex);
                throw ex;
            }
        }
    }
    
    static protected SessionFactory buildFactory(Configuration cfg)
    throws Exception 
    {
        SessionFactory factory = null;
        try {
            factory = cfg.buildSessionFactory();
        } catch (Exception ex) {
            log.error("Could not build the factory", ex);
            closeFactory(factory);
            factory = null;
            throw ex;
        }
        return factory;
    }
  
    static public void closeFactory(SessionFactory factory) {
        if (factory != null) {
            factory.close();
        }
    }
  
    static public void closeFactory() {
        closeFactory(sessionFactory);
    }

    static public void saveDB(Object obj) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
      
            session.save(obj);
      
            tx.commit();
        } finally {
            if (session != null) session.close();
        }
    }
  
    static public void removeDB(Object obj) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
      
            session.delete(obj);
      
            tx.commit();
        } finally {
            if (session != null) session.close();
        }
    } 
  
    static public Object getFirstMatch(Class classBean, String strKey, Object value) {
        java.util.List records = getListData(classBean, strKey, value);
        if (records != null && records.size() > 0) {
            return records.get(0);
        }
        return null;
    }
  
    static public Object getFirstMatch(Object data, String strKey, Object value) {
        return getFirstMatch(data.getClass(), strKey, value);
    }
  
    static public Object getKeyData(Class beanClass, long itemId)
    {
        Object data = null;
        Session session = sessionFactory.openSession();
    
        data = session.get(beanClass, itemId);
    
        session.close();
    
        return data;
    }
  
    static public Exception getLastError() {
        return lastError;
    }
}
