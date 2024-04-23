package com.hrimch.inventoryservice.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter <Jwt , AbstractAuthenticationToken> {

    //dieses Object sucht authorities/roles in jwt
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    //überschreiben von Methode convert von interface Converter
    // erwartet einen JWT udn return Collection von grantedAuthorities
    // untersucht jwt und extrahiert roles
    @Override
    public AbstractAuthenticationToken convert (Jwt jwt) {
        // wird Colletion erstellt und danach returned von Typ GrantedAuthority
        //Stream.concat(stream1, stream2) werden 2 Collections konkateniert
        //jwtGrantedAuthoritiesConverter.convert(jwt).stream()  return Collection von grantedAuthority in jwt "scope": "email profile" (par default)
        //extractResourceRoles(jwt).stream() macht das gleiche nur jetzt mit vordefinierten Keys, in diesem Fall  "realm_access": {
        //    "roles": [
        //      "offline_access",
        //      "uma_authorization",
        //      "default-roles-inventory-service"
        //    ]
        //  }
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());
        // "preferred_username": "service-account-dashboard-inventory",
        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaim("preferred_username"));
    }

    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        //Map von extrakten Roles
        //  "realm_access": {
        //    "roles": [
        //      "offline_access",
        //      "uma_authorization",
        //      "default-roles-inventory-service"
        //    ]
        //  }
        Map<String, Object> realmAccess;
        Collection<String> roles;
        if(jwt.getClaim("realm_access") == null){
            return Set.of();
        }
        realmAccess = jwt.getClaim("realm_access");
        System.out.println("realm_access: "+ realmAccess.toString());
        roles = (Collection<String>) realmAccess.get("roles");
        System.out.println("roles "+ roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toSet()).toString());
        return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toSet());
    }


}
