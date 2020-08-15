package proPets.gateway.filters.pre;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import proPets.gateway.configuration.GatewayConfiguration;
import proPets.gateway.dto.AuthResponse;

@Component
public class JWTPreZuulFilter extends ZuulFilter {

	@Autowired
	GatewayConfiguration gatewayConfiguration;

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest req = ctx.getRequest();
		String path = getPath(ctx);
		String method = req.getMethod();
		boolean res = !checkPointCut(path, method);
		return res;
	}

	@Override
	public int filterOrder() {
		return 7;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest req = ctx.getRequest();
		HttpServletResponse resp = ctx.getResponse();

		if (req.getHeader("Authorization") != null) {
			String auth = req.getHeader("Authorization");

			if (auth.startsWith("Bearer")) {
				System.out.println("here");
				String newToken;
				String email;
				try {
					ResponseEntity<AuthResponse> newResponse = getHeadersWithNewToken(auth, ctx);

					newToken = newResponse.getHeaders().getFirst("X-token");
					email = newResponse.getBody().getEmail();
					resp.setHeader("X-token", newToken);

					ctx.addZuulRequestHeader("authorId", email);

					ctx.setResponseStatusCode(HttpStatus.OK.value());
					return null;

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("RestTemplate exception: token is not valid or JWT server does not response");
					ctx.unset();
					ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
				}
			} else {
				System.out.println("Header does not contains BEARER");
				ctx.unset();
				ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());

				return null;
			}
		} else {
			System.out.println("No header \"Authorization\" or this endpoint is in special list");
			ctx.unset();
			ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
			return null;
		}
		return null;
	}

	// might be false
	private boolean checkPointCut(String path, String method) {
		boolean check = "/message/v1/post/cleaner".equalsIgnoreCase(path) && "DELETE".equalsIgnoreCase(method);
		check = check || ("/lostFound/lost/v1/post/cleaner".equalsIgnoreCase(path) && "DELETE".equalsIgnoreCase(method))
				|| ("/lostFound/found/v1/post/cleaner".equalsIgnoreCase(path) && "DELETE".equalsIgnoreCase(method));
		check = check || path.contains("account") || path.contains("send") || path.contains("search")
				|| path.startsWith("/convert") || path.startsWith("/lostFound/lost/v1/all_matched")
				|| path.startsWith("/lostFound/lost/v1/new_matched") || path.startsWith("lost/v1/accessCode");
		check = check || path.startsWith("/lostFound/found/v1/all_matched")
				|| path.startsWith("/lostFound/found/v1/new_matched");
		return check;
	}

	private ResponseEntity<AuthResponse> getHeadersWithNewToken(String auth, RequestContext ctx) {
		RestTemplate restTemplate = gatewayConfiguration.restTemplate();

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Authorization", auth);
		headers.add("Content-Type", "application/json");

		String url = gatewayConfiguration.getBaseJWTUrl() + "account/v1/verify";
		RequestEntity<Object> request = new RequestEntity<>(headers, HttpMethod.POST, URI.create(url));
		ResponseEntity<AuthResponse> newResponse = restTemplate.exchange(request, AuthResponse.class);
		return newResponse;
	}

	private String getPath(RequestContext context) {
		HttpServletRequest request = context.getRequest();
		StringBuilder builder = new StringBuilder();
		builder.append(request.getContextPath()).append(request.getServletPath());
		if (request.getPathInfo() != null) {
			builder.append(request.getPathInfo());
		}
		if (context.getRequestQueryParams() != null) {
			builder.append(context.getRequestQueryParams());
		}
		return builder.toString();
	}
}
