package enhanced;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.Map;


public abstract class HelperBase {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected Logger logger;
    
    protected Map<String, Map<String, String>> checked =
            new HashMap<String, Map<String, String>>();
    protected Map<String, Map<String, String>> selected =
            new HashMap<String, Map<String, String>>();
    
    private Method methodDefault = null;

    public enum SessionData {READ, IGNORE};

    protected abstract void copyFromSession(Object helper);
    
    public HelperBase(HttpServletRequest request,
            HttpServletResponse response) 
    {
        this.request = request;
        this.response = response;
        logger = Logger.getLogger("newlogger");
        logger.setLevel(Level.DEBUG);
    }
    
    public void addHelperToSession(String name, SessionData state) {
        if (SessionData.READ == state) {
            Object sessionObj = request.getSession().getAttribute(name);

            if (sessionObj != null) {
                copyFromSession(sessionObj);
            }
        }
        request.getSession().setAttribute(name, this);
    }
    
    protected String executeButtonMethod() throws ServletException, IOException {
        String result = "";
        methodDefault = null;
        Class clazz = this.getClass();
        Class enclosingClass = clazz.getEnclosingClass();
        while (enclosingClass != null) {
            clazz = this.getClass();
            enclosingClass = clazz.getEnclosingClass();
        }
        try {
            result = executeButtonMethod(clazz, true);
        } 
        catch (Exception ex) {
            return "";
        }
        return result;
    }
    
    protected String executeButtonMethod(Class clazz, boolean searchForDefault)
        throws IllegalAccessException, InvocationTargetException
    {
        String result = "";
        Method [] methods = clazz.getDeclaredMethods();
        for(Method method : methods) {
            ButtonMethod annotation = method.getAnnotation(ButtonMethod.class);
            if (annotation != null) {
                if (searchForDefault && annotation.isDefault())
                {
                    methodDefault = method;
                }
                if (request.getParameter(annotation.buttonName()) != null)
                {
                    result = invokeButtonMethod(method);
                    break;
                }
            }
        }
        
        if (result.equals("")) {
            Class superClass = clazz.getSuperclass();
            if (superClass != null) {
                result = executeButtonMethod(superClass, methodDefault == null);
            }
            if (result.equals("")) {
                if (methodDefault != null) {
                    result = invokeButtonMethod(methodDefault);
                } 
                else {
                    logger.error("(executeButtonMethod) No default method " +
                                 "was specified, but one was needed.");
                    result = "No default method was specified,.";
                }
            }
        }
        return result;
    }
    
    protected String invokeButtonMethod(Method buttonMethod)
            throws IllegalAccessException, InvocationTargetException
    {
        String resultInvoke = "Could not invoke method";
        try{
            resultInvoke = (String) buttonMethod.invoke(this, (Object[]) null);
        } 
        catch (IllegalAccessException iae) {
            logger.error("(invoke) Button method is not public.", iae);
            throw iae;
        } 
        catch (InvocationTargetException ite) {
            logger.error("(invoke) Button method exception", ite);
            throw ite;
        }
        return resultInvoke;
    }
    
    public void fillBeanFromRequest(Object data) {
        try {
            org.apache.commons.beanutils.BeanUtils.populate(data, request.getParameterMap());
        } 
        catch (IllegalAccessException iae) {
            logger.error("Populate - Illegal Access.", iae);
        } 
        catch (InvocationTargetException ite) {
            logger.error("Populate - Invocation Target.", ite);
        }
    }
    
    protected void setCheckedAndSelected(Object data) {
        setCheckedAndSelected(data, data.getClass());
    }
    
    protected void setCheckedAndSelected(Object data, Class clazz) {
        Method[] allMethods = clazz.getDeclaredMethods();
        
        for (Method method : allMethods) {
            SetByAttribute propAnnotation = method.getAnnotation(SetByAttribute.class);

            if (propAnnotation != null) {
                String property = method.getName();
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("get(.+)");
                java.util.regex.Matcher matcher = pattern.matcher(property);

                if (!matcher.matches()) {
                    logger.error(property + " must be an accessor.");
                } else {
                    property = matcher.group(1);
                    property = property.substring(0, 1).toLowerCase() + property.substring(1);
                    clearProperty(property, propAnnotation.type());
                    if (method.getReturnType().isArray()) {
                        Object[] result = (Object[]) invokeGetter(data, method);
                        if (result != null) {
                            for (Object obj : result) {
                                addChoice(property, obj.toString(), (AttributeType) propAnnotation.type());
                            }
                        }
                    } else {
                        Object result = invokeGetter(data, method);
                        if (result != null) {
                            addChoice(property, result.toString(), (AttributeType) propAnnotation.type());
                        }
                    }
                }
            }
        }

        Class parentClass = clazz.getSuperclass();
        if (parentClass != null) {
            setCheckedAndSelected(data, parentClass);
        }
    }
    
    protected Object invokeGetter(Object obj, Method method) {
        Object result = null;
        try {
            result = method.invoke(obj, (Object[]) null);
        } catch (IllegalAccessException iae) {
            logger.error("(invoke) Accessor needs public access", iae);
        } catch (InvocationTargetException ite) {
            logger.error("(invoke) Accessor threw an exception", ite);
        }
        return result;
    }
    
    public void addChoice(String list, String item, AttributeType type) {
        if (type == null) {
            return;
        }
        if (AttributeType.CHECKED == type) {
            addChecked(list, item);
        }
        if (AttributeType.SELECTED == type) {
            addSelected(list, item);
        }
    }
    
    public void clearProperty(String property, AttributeType type) {
        Map<String, String> propMap;
        if (AttributeType.CHECKED == type) {
            propMap = checked.get(property);
            if (propMap != null) {
                propMap.clear();
            }
        } else if (AttributeType.SELECTED == type) {
            propMap = selected.get(property);
            if (propMap != null) {
                propMap.clear();
            }
        }
    }
    
    public Map getChecked() {
        return checked;
    }

    public Map getSelected() {
        return selected;
    }
    
    public void addChecked(String group, String item) {
        if (checked.get(group) == null) {
            checked.put(group, new HashMap<String, String>());
        }
        checked.get(group).put(item, "checked");
    }

    public void addSelected(String list, String item) {
        if (selected.get(list) == null) {
            selected.put(list,
                    new HashMap<String, String>());
        }
        selected.get(list).put(item, "selected");
    }
    
    public void clearMaps() {
        checked.clear();
        selected.clear();
    }
}
