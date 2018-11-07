package com.softactive.core.utils;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;
import com.softactive.core.object.ApiUser;
import com.softactive.core.service.AuthenticationService;
import com.softactive.core.service.UserDetailsService;
import com.softactive.editor.common.page.LoginBean;

public class UserApiFilter implements Filter {
 
 
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
 
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        LoginBean loginBean = (LoginBean) SpringUtil.findBean("loginBean");
        loginBean.setIp(getIpAddr(request));
        loginBean.setServletParameterMap(getRequestHeadersInMap(request));
        
        String accessToken = request.getParameter("access_token");
        if (accessToken==null) {
        	chain.doFilter(request, response);
        }else if (accessToken.length()>0) {
        	try {
        		String sURL = "https://api.quantybox.com/v1/users/me?access_token=" + accessToken + "&ip_address=" + loginBean.getIp() + "&project_code=DER";
            	URL myURL = new URL(sURL);
            	HttpURLConnection urlConnection = (HttpURLConnection) myURL.openConnection();
            	urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0");
            	urlConnection.setRequestMethod("GET");
            	urlConnection.setRequestProperty("Accept", "application/json");
            	BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            	String jsonText = "";
            	String inputLine = "";
            	while ((inputLine = in.readLine()) != null){
            		jsonText += inputLine;
            	}
            	ApiUser apiUser = new GsonBuilder().create().fromJson(jsonText, ApiUser.class);
            	if (apiUser!=null) {
            		AuthenticationService authenticationService = (AuthenticationService) SpringUtil.findBean("authenticationService");
            		UserDetailsService userDetailsService = (UserDetailsService) SpringUtil.findBean("userDetailsService");
            		userDetailsService.addApiUser(apiUser);
            		if (authenticationService.login(apiUser.getEmail(), apiUser.getEmail())) {
            			response.sendRedirect("/GlobalRiskWatch/pages/home.jsf");
            		}
            	}
        	}catch(Exception e) {
        		response.sendRedirect("https://dashboard.quantybox.com");
        	}
        	
        }else {
        	response.sendRedirect("https://dashboard.quantybox.com");
        }
    }
    
    @SuppressWarnings("rawtypes")
	private Map<String, String> getRequestHeadersInMap(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            result.put(key, value);
        }
        return result;
    }
    
    public String getIpAddr(HttpServletRequest request) {
    	String ip = "";
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    		ip = request.getHeader("x-forwarded-for");
    	}
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    		ip = request.getHeader("cf-connecting-ip");
    	}
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    		ip = request.getHeader("Proxy-Client-IP");
    	}
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    		ip = request.getHeader("WL-Proxy-Client-IP");
    	}
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    		ip = request.getRemoteAddr();
    	}
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    		ip = request.getHeader("x-real-ip");
    	}
    	return ip;
    }
    
    public static String connectionGet(String url, String parameter) throws MalformedURLException, ProtocolException, IOException {

        URL url1 = new URL(url);
        HttpURLConnection request1 = (HttpURLConnection) url1.openConnection();
        request1.setRequestMethod("GET");
        request1.connect();
        String responseBody = convertStreamToString(request1.getInputStream());
        return responseBody;
    }
    
    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }

        return sb.toString();
    }

	@Override
	public void destroy() {
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
}