//Zachary Pell

package enhanced;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller extends HttpServlet {

    @Override
    public void init() {
        boolean create =
                Boolean.parseBoolean(getInitParameter("create"));
        ControllerHelper.initHibernate(create);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        ControllerHelper helper = new ControllerHelper(request, response);
        helper.doGet();
    }
    
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        ControllerHelper helper =
                new ControllerHelper(request, response);
        helper.doPost();
    }

}
