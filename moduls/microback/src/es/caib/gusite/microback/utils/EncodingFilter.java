package es.caib.gusite.microback.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodingFilter implements Filter {
	protected String encoding = null;  
    protected FilterConfig filterConfig = null;  
    protected boolean ignore = true;  
   
    /** 
     * Take this filter out of service. 
     */  
    public void destroy() {  
        this.encoding = null;  
        this.filterConfig = null;  
    }  
   
    /** 
     * Select and set (if specified) the character encoding to be used to 
     * interpret request parameters for this request. 
     */  
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)  
            throws IOException, ServletException {  
        // Conditionally select and set the character encoding to be used  
        if (ignore || (request.getCharacterEncoding() == null)) {  
            String encoding = selectEncoding(request);  
            if (encoding != null) request.setCharacterEncoding(encoding);  
        }    // Pass control on to the next filter  
        chain.doFilter(request, response);  
    }  
   
    /** 
     * Place this filter into service. 
     */  
    public void init(FilterConfig filterConfig) throws ServletException {  
        this.filterConfig = filterConfig;  
        this.encoding = filterConfig.getInitParameter("encoding");  
        String value = filterConfig.getInitParameter("ignore");  
        if (value == null)  
            this.ignore = true;  
        else if (value.equalsIgnoreCase("true"))  
            this.ignore = true;  
        else if (value.equalsIgnoreCase("yes"))  
            this.ignore = true;  
        else this.ignore = false;  
    }  
   
    /** 
     * Select an appropriate character encoding to be used, based on the 
     * characteristics of the current request and/or filter initialization 
     * parameters.  If no character encoding should be set, return 
     * null. 
     */  
    protected String selectEncoding(ServletRequest request) {  
        return (this.encoding);  
    }  

}