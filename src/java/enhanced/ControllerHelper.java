package enhanced;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.util.ArrayList;

public class ControllerHelper extends HelperBase{
    
    protected RequestData data = new RequestData();

    public ControllerHelper(HttpServletRequest request,
                            HttpServletResponse response) 
    {
        super(request, response);
    }

    public Object getData() {
        return data;
    }
    
    protected String jspLocation(String page) {
        return "/WEB-INF/enhanced/" + page;
    }
    
    @ButtonMethod(buttonName="browseButton")
    public String browseMethod() {
        java.util.List list =
                HibernateHelper.getListData(data.getClass());
        request.setAttribute("database", list);
        return jspLocation("browse.jsp");
    }
    
    @ButtonMethod(buttonName="newButton")
    public String newMethod() {
        return jspLocation("new.jsp");
    }
    
    @ButtonMethod(buttonName="submitButton")
    public String submitMethod() {
        fillBeanFromRequest(data);
        HibernateHelper.updateDB(data);
        java.util.List list =
                HibernateHelper.getListData(data.getClass());
        request.setAttribute("database", list);
        return jspLocation("browse.jsp");
    }
    
    @ButtonMethod(buttonName="searchButton")
    public String searchMethod() {
        return jspLocation("search.jsp");
    }
    
    @ButtonMethod(buttonName="goButton")
    public String goMethod() {
        fillBeanFromRequest(data);
        Object dataPersistent = 
                HibernateHelper.getFirstMatch(data, "name", data.getName());
        if (dataPersistent != null) {
            data = (RequestData) dataPersistent;
        }
        java.util.List list = new ArrayList<>();
        list.add(data);
        request.setAttribute("database", list);
        return jspLocation("browse.jsp");
    }
    
    protected void doGet() throws ServletException, java.io.IOException {
        if (HibernateHelper.testDB(response)){
            addHelperToSession("helper", SessionData.IGNORE);
            String address = executeButtonMethod();
            request.getRequestDispatcher(address).forward(request, response);
        }
    }
    
    protected void doPost() throws ServletException, java.io.IOException {
        if (HibernateHelper.testDB(response)) {
            addHelperToSession("helper", SessionData.READ);

            String address = executeButtonMethod();

            request.getRequestDispatcher(address).forward(request, response);
        }
    }
    
    @Override
    public void copyFromSession(Object sessionHelper) {
        if (sessionHelper.getClass() == this.getClass()) {
            ControllerHelper oldHelper = (ControllerHelper)sessionHelper;
            data = oldHelper.data;
            checked = oldHelper.checked;
            selected = oldHelper.selected;
        }
    }
    
    static public void initHibernate(boolean create) {
        Properties props = new Properties();
        props.setProperty("hibernate.dialect",
                "org.hibernate.dialect.MySQLDialect");
        props.setProperty("hibernate.connection.driver_class",
                "com.mysql.jdbc.Driver");
        props.setProperty("hibernate.c3p0.min_size", "1");
        props.setProperty("hibernate.c3p0.max_size", "5");
        props.setProperty("hibernate.c3p0.timeout", "300");
        props.setProperty("hibernate.c3p0.max_statements",
                "50");
        props.setProperty("hibernate.c3p0.idle_test_period",
                "300");
        props.setProperty("hibernate.connection.url",
                "jdbc:mysql://localhost:3306/fortnitedb");
        props.setProperty("hibernate.connection.username",
                "root");
        props.setProperty("hibernate.connection.password",
                "password");

        if (create) {
            HibernateHelper.createTable(
                    props,
                    RequestData.class);
        }

        HibernateHelper.initSessionFactory(
                props,
                RequestData.class);
    }
}
