package crdb.jmsapp.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import crdb.jmsapp.domain.Customer;
import crdb.jmsapp.domain.Roles;
import crdb.jmsapp.service.CustomerService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
@Slf4j
public class CustomerResource {
    private final CustomerService customerService;

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getCustomers(){
        return ResponseEntity.ok().body(customerService.getCustomers());

    }

    @PostMapping("/customer/add")
    public ResponseEntity<Object> saveCustomer(@RequestBody Customer customer){
//        check if the user exists in the db

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/customer/add").toUriString());
        log.info("URI "+uri.toString());

        return ResponseEntity.created(uri).body(customerService.saveCustomer(customer));
    }

    @PostMapping("/role/addRole")
    public ResponseEntity<Roles> saveRoles(@RequestBody Roles roles){
        return ResponseEntity.ok().body(customerService.saveRoles(roles));
    }

    @PostMapping("/role/addToUser")
    public ResponseEntity<Object> addRoleToUser(@RequestBody RoleToCustomer roleToCustomer){
        customerService.addRoleToCustomer(roleToCustomer.getUsername(), roleToCustomer.getRolename());
        return ResponseEntity.ok().body(customerService.addRoleToCustomer(roleToCustomer.getUsername(),roleToCustomer.getRolename()));
    }

    @PostMapping("/token/refresh")
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader!= null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
//             Load the user
                Customer user =  customerService.getCustomer(username);
                String access_token = JWT.create().withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 *60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getRoles().stream().map(Roles::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);

            }catch(Exception e){
                log.error("Error logging in {}",e.getMessage());
                response.setStatus(FORBIDDEN.value());
                response.setHeader("error",e.getMessage());
                Map<String,String> error = new HashMap<>();
                error.put("error_message",e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);

            }


        }else{
            throw new RuntimeException("Refresh token is missing");
        }
    }
}



@Data
class RoleToCustomer{
    String username;
    String rolename;
}