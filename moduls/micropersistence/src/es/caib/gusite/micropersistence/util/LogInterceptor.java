package es.caib.gusite.micropersistence.util;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Intercepta las llamadas a métodos y añade logs.
 * 
 * @author Indra
 */
public class LogInterceptor implements MethodInterceptor, Serializable {

	private static final long serialVersionUID = 1L;
	private transient Log log;
    private final String name;

    public LogInterceptor(String name) {
        this.name = name;
    }

    private Log getLog() {
        if (log == null) {
            log = LogFactory.getLog(name);
        }
        return log;
    }

    private boolean invokeSuper(Object obj, Method method, Object[] params) {
        String className = method.getDeclaringClass().getName();
        className = className.substring(className.lastIndexOf('.') + 1, className.length());
        getLog().info("-> " + className + "." + method.getName() + Arrays.asList(params));
        return true;
    }

    private Object afterReturn(Object obj, Method method, Object[] params, boolean b, Object result, Throwable excepcion) throws Throwable {
        if (excepcion != null) {
            throw excepcion.fillInStackTrace();
        }

        String logText = "<- ";
        if (result != null) {
            if (result instanceof Collection<?>) {
                logText += " (" + ((Collection<?>) result).size() + ") " + result;
            } else if (result.getClass().isArray()) {
                Object[] objects = (Object[]) result;
                logText += " (" + objects.length + ") " + Arrays.asList(objects);
            } else {
                logText += result;
            }
        }
        getLog().info(logText);
        return result;
    }

    public Object intercept(Object obj, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
        boolean llamarSuper = invokeSuper(obj, method, params);
        Object result = null;
        Throwable exception = null;

        if (llamarSuper) {
            try {
                result = methodProxy.invokeSuper(obj, params);
            } catch (Throwable throwable) {
                exception = throwable;
            }
        }

        return afterReturn(obj, method, params, llamarSuper, result, exception);
    }

}
