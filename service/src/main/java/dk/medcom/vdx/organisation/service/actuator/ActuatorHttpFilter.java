package dk.medcom.vdx.organisation.service.actuator;

import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.context.UserRole;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class ActuatorHttpFilter extends HttpFilter {
    private UserContextService userContextService;

    public ActuatorHttpFilter(UserContextService userContextService) {
        this.userContextService = userContextService;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        if(!userContextService.hasAnyNumberOfRoles(Arrays.asList(UserRole.MONITOR))) { // TODO Enable again.
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            return;
//        }

        chain.doFilter(request, response);
    }
}
