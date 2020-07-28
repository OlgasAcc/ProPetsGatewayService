package proPets.gateway.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(10)

public class CorsFilter implements Filter {
	
	@Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        final HttpServletResponse response = (HttpServletResponse) res;
        final HttpServletRequest request = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers",
				"Authorization, X-Token, X-id, Origin, Content-Type, Accept");
		response.setHeader("Access-Control-Expose-Headers", "Authorization, xsrf-token, X-Token, X-id");
        response.setHeader("Access-Control-Max-Age", "3600");
       
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
        	String reqMethod = (request.getHeader("Access-Control-Request-Method"));
        	String reqHeader = (request.getHeader("Access-Control-Request-Header"));
        	response.setHeader("Access-Control-Allow-Methods", reqMethod);
        	response.setHeader("Access-Control-Allow-Headers", reqHeader);
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
	
}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
//			throws IOException, ServletException {
//		HttpServletRequest request = (HttpServletRequest) req;
//		HttpServletResponse response = (HttpServletResponse) res;
//		final String origin = (request.getHeader("Origin"));
//
//		if (origin != null && origin.equals("http://localhost:3000")) {
//			response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
//			response.addHeader("Access-Control-Allow-Credentials", "true");
//			response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
//			response.addHeader("Access-Control-Max-Age", "86400"); 
//			// response.addHeader("Access-Control-Allow-Headers",
//			// "Authorization, X-Token, X-id, Origin, Content-Type, Accept,
//			// Access-Control-Request-Method, Access-Control-Request-Headers");
//			// response.addHeader("Access-Control-Expose-Headers", "Authorization,
//			// xsrf-token, X-Token, X-id");
//
//			if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
//				response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//				response.addHeader("Access-Control-Allow-Headers",
//						"Authorization, X-Token, X-id, Origin, Content-Type, Accept, Access-Control-Request-Method, Access-Control-Request-Headers");
//				response.addHeader("Access-Control-Expose-Headers", "Authorization, xsrf-token, X-Token, X-id");
//				response.setStatus(200);
//			}
//
//			chain.doFilter(request, res);
//		}
//	}
//}