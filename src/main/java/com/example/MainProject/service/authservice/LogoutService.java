package com.example.MainProject.service.authservice;
import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.entities.token.Tokens;
import com.example.MainProject.exception.GenericException;
import com.example.MainProject.others.EmailSender;
import com.example.MainProject.repository.RoleRepository;
import com.example.MainProject.repository.TokenRepo;
import com.example.MainProject.repository.UserRepository;
import com.example.MainProject.security.JwtGenerator;
import com.example.MainProject.security.SecurityConstant;
import com.example.MainProject.service.authservice.Impl.LogoutServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutServiceImpl {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtGenerator jwtGenerator;
    private EmailSender emailSender;
    private TokenRepo tokenRepo;

    @Autowired

    public LogoutService(AuthenticationManager authenticationManager,
                        UserRepository userRepository, RoleRepository roleRepository,
                        PasswordEncoder passwordEncoder, JwtGenerator jwtGenerator, EmailSender emailSender,
                        TokenRepo tokenRepo) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator=jwtGenerator;
        this.emailSender=emailSender;
        this.tokenRepo=tokenRepo;

    }
    Logger logger= LoggerFactory.getLogger(LogoutService.class);

    @Value("${admin.username}")
    String adminEmail;

    @Override
   public GenerateResponse logoutCustomer(String token){
        logger.warn("checking token validation");
        boolean isValid=jwtGenerator.validateToken(token);
        if(!isValid) {
            throw new GenericException("token is inValid", HttpStatus.INTERNAL_SERVER_ERROR);
        }
            String email = jwtGenerator.getUserNameFromJwt(token);

            if(email.equals(adminEmail)){
                throw new GenericException("admin cant be logged out of the system",HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Tokens tokens = tokenRepo.findByEmail(email).orElse(null);
            if (tokens == null) {
                logger.info("token is not present in tokenRepo");
                throw new GenericException("user doesn't exist or already logged out", HttpStatus.NOT_FOUND);
            }

            tokenRepo.deleteByEmail(email);
            // tokenRepo.save(tokens);
            logger.info("Success : giving control back to controller");
            return new GenerateResponse("you have been logged out");

   }
}
