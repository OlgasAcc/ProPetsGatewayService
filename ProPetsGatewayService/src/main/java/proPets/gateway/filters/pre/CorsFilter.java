package proPets.gateway.filters.pre;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import proPets.gateway.configuration.GatewayConfiguration;

@Component
public class CorsFilter extends ZuulFilter {

	@Autowired
	GatewayConfiguration gatewayConfiguration;

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		HttpServletResponse response = ctx.getResponse();

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, DELETE");
		response.setHeader("Access-Control-Allow-Headers",
				"Authorization, X-Token, X-id, Origin, Content-Type, Accept");
		response.setHeader("Access-Control-Expose-Headers", "Authorization, xsrf-token, X-Token, X-id");
		response.setHeader("Access-Control-Max-Age", "3600");

		if ("OPTIONS".equalsIgnoreCase(RequestContext.getCurrentContext().getRequest().getMethod())) {
			String reqMethod = (request.getHeader("Access-Control-Request-Method"));
			String reqHeader = (request.getHeader("Access-Control-Request-Header"));
			response.setHeader("Access-Control-Allow-Methods", reqMethod);
			response.setHeader("Access-Control-Allow-Headers", reqHeader);
			response.setStatus(HttpServletResponse.SC_OK);
			ctx.unset();
			ctx.setResponseStatusCode(HttpStatus.OK.value());
			return null;
		} else {
			return null;
		}
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 5;
	}

	@Override
	public String filterType() {
		return "pre";
	}
}