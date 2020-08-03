package proPets.gateway.filters.pre;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
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
		String path = req.getContextPath();
		String method = req.getMethod();
		boolean res = !checkPointCut(path, method);
		System.out.println(res);
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
		String path = req.getContextPath();
		String method = req.getMethod();
		System.out.println(path);
		
		if (req.getHeader("Authorization") != null && !checkPointCut(path, method)) {
			String auth = req.getHeader("Authorization");

//		if (auth == null && !path.contains("sign_up")) {
//		ctx.unset();
//			ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
//
//		} else {

			// if (!path.contains("account") && !checkPointCut(path, method) &&
			// !checkStartPathAdditional(path)) {

			if (auth.startsWith("Bearer")) {
				System.out.println("here");
				String newToken;
				String email;
				try {
					ResponseEntity<AuthResponse> newResponse = getHeadersWithNewToken(auth);
					newToken = newResponse.getHeaders().getFirst("X-token");
					email = newResponse.getBody().getEmail();
					resp.setHeader("X-token", newToken);

					ctx.addZuulRequestHeader("authorId", email);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					throw new Exception("Header Authorization is not valid");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
//		}
		System.out.println("validation filter did not work");
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

	// might be false
	// private boolean checkStartPathAdditional(String path) {
	// boolean check = path.contains("account") || path.contains("send") ||
	// path.contains("search")
	// || path.startsWith("/convert") ||
	// path.startsWith("/lostFound/lost/v1/all_matched")
	// || path.startsWith("/lostFound/lost/v1/new_matched") ||
	// path.startsWith("lost/v1/accessCode");
	// check = check || path.startsWith("/lostFound/found/v1/all_matched")
	// || path.startsWith("/lostFound/found/v1/new_matched");
	// return check;
	// }

	private ResponseEntity<AuthResponse> getHeadersWithNewToken(String auth) {
		RestTemplate restTemplate = gatewayConfiguration.restTemplate();

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Authorization", auth);
		headers.add("Content-Type", "application/json");
		
		String url = gatewayConfiguration.getBaseJWTUrl() + "account/v1/verify";
		try {
			RequestEntity<Object> request = new RequestEntity<>(headers, HttpMethod.POST, URI.create(url));
			ResponseEntity<AuthResponse> newResponse = restTemplate.exchange(request, AuthResponse.class);
			if (newResponse.getStatusCode().is2xxSuccessful() && newResponse.getBody().getEmail()!=null) {
				return newResponse;
			} else {
				throw new RuntimeException("Validation is failed: wrong token or userId = null");
			}
		} catch (HttpClientErrorException e) {
			throw new RuntimeException("Validation is failed");
		}
	}

}
