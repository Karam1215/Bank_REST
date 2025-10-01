    package com.example.bankcards.security;

    import org.springframework.security.authorization.AuthorizationDecision;
    import org.springframework.security.authorization.AuthorizationManager;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
    import org.springframework.stereotype.Component;

    import java.util.function.Supplier;

    @Component
    public class UserSecurity implements AuthorizationManager<RequestAuthorizationContext> {

        @Override
        public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext ctx) {
            String userId = ctx.getVariables().get("userId");

            Authentication authentication = authenticationSupplier.get();
            String authenticatedUserId = getUsernameFromAuthentication(authentication);

            boolean isAuthorized = authenticatedUserId != null && authenticatedUserId.equals(userId);
            return new AuthorizationDecision(isAuthorized);
        }

        private String getUsernameFromAuthentication(Authentication authentication) {
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
                authentication.getPrincipal();
                return userPrincipal.getUsername();
            }
            return null;
        }
    }