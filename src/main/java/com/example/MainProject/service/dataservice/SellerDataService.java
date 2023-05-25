package com.example.MainProject.service.dataservice;

import com.example.MainProject.dto.*;
import com.example.MainProject.entities.token.Tokens;
import com.example.MainProject.entities.users.Address;
import com.example.MainProject.entities.users.Seller;
import com.example.MainProject.entities.users.User;
import com.example.MainProject.exception.GenericException;
import com.example.MainProject.exception.SellerException;
import com.example.MainProject.others.EmailSender;
import com.example.MainProject.repository.AddressRepository;
import com.example.MainProject.repository.SellerRepository;
import com.example.MainProject.repository.TokenRepo;
import com.example.MainProject.repository.UserRepository;
import com.example.MainProject.security.JwtGenerator;
import com.example.MainProject.service.dataservice.Impl.SellerDataServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Service
public class SellerDataService implements SellerDataServiceImpl {

    @Autowired
    JwtGenerator jwtGenerator;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EmailSender emailSender;
    @Autowired
    TokenRepo tokenRepo;
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    String base_path="/home/shashank/Shashank_Daksh_6980/MainProject/src/" +
            "main/java/com/example/MainProject/Images/sellerImage/";

    Logger logger= LoggerFactory.getLogger(SellerDataService.class);


    @Override
    public SellerDataListDto getSellerData(String token) {
        SellerDataListDto sellerDataListDto=new SellerDataListDto();
        logger.warn("checking token validation");
        boolean isValid = jwtGenerator.validateToken(token);
        if(!isValid){
            throw new SellerException("Token not valid",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(!isTokenExpire(token)){
            throw new SellerException("token is expired for this user or the user " +
                    "has logged out ,please login again",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }


        String email= jwtGenerator.getUserNameFromJwt(token);
        Seller seller=sellerRepository.findByEmail(email).orElse(null);
        if(Objects.isNull(seller)){
            throw new SellerException("seller not found",HttpStatus.NOT_FOUND);
        }
        if(!seller.isActive()){
            logger.error("seller not active");
            throw new SellerException("Seller is not Active",HttpStatus.FORBIDDEN);
        }

        logger.info("calling helper function");
        getSellerDataHelper(sellerDataListDto,seller);
        logger.info("response send");
        return sellerDataListDto;

    }
    public void getSellerDataHelper(SellerDataListDto sellerDataListDto,Seller seller){
        sellerDataListDto.setId(seller.getId());
        sellerDataListDto.setLastName(seller.getLastname());
        sellerDataListDto.setFirstName(seller.getFirstName());
        sellerDataListDto.setAddress(seller.getAddress());
        sellerDataListDto.setGst(seller.getGstNumber());
        sellerDataListDto.setActive(seller.isActive());
        sellerDataListDto.setCompanyContact(seller.getCompanyContact());
        sellerDataListDto.setAddress(seller.getAddress());
        sellerDataListDto.setImage(seller.getImagePath());
    }

    @Override
    public GenerateResponse upDateSeller(String token, SellerUpdateDto sellerUpdateDto) {
        logger.warn("validating token");
        boolean isValid=jwtGenerator.validateToken(token);
        if(!isValid){
            throw new SellerException("Token not Valid",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String email= jwtGenerator.getUserNameFromJwt(token);
        Seller seller=sellerRepository.findByEmail(email).orElse(null);
        if(Objects.isNull(seller)){
            throw new SellerException("seller not found",HttpStatus.NOT_FOUND);
        }
        if(!seller.isActive()){
            logger.error("seller not active");
            throw new SellerException("Seller is not Active",HttpStatus.FORBIDDEN);
        }
        if(!isTokenExpire(token)){
            throw new SellerException("token is expired for this user or the user " +
                    "has logged out ,please login again",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(sellerRepository.existsByCompanyNameIgnoreCase(sellerUpdateDto.getCompanyName())){
            throw new SellerException("Company name is not unique",HttpStatus.INTERNAL_SERVER_ERROR);
        }


        logger.info("calling helper function");
        setSellerHelper(seller,sellerUpdateDto);
        sellerRepository.save(seller);

        return new GenerateResponse("Entered field updated");
    }
    public void setSellerHelper(Seller seller,SellerUpdateDto sellerUpdateDto){
        if(sellerUpdateDto.getFirstName()!=null)
            seller.setFirstName(sellerUpdateDto.getFirstName().trim());
        if(sellerUpdateDto.getLastName()!=null)
            seller.setLastname(sellerUpdateDto.getLastName().trim());
        if(sellerUpdateDto.getGst()!=null)
            seller.setGstNumber(sellerUpdateDto.getGst());
        if(sellerUpdateDto.getCompanyName()!=null)
            seller.setCompanyContact(sellerUpdateDto.getCompanyName().trim());
        if(sellerUpdateDto.getCompanyName()!=null)
            seller.setCompanyName(sellerUpdateDto.getCompanyName());
    }

    @Override
    public GenerateResponse upDatePassword(String token, ForgotPasswordDto forgotPasswordDto) {
        logger.warn("checking token validation");
        boolean isValid=jwtGenerator.validateToken(token);
        if(!isValid){
            throw new GenericException( "Token Invalid",HttpStatus.INTERNAL_SERVER_ERROR);
        }


        String email=jwtGenerator.getUserNameFromJwt(token);
        Seller seller=sellerRepository.findByEmail(email).orElse(null);
        if(Objects.isNull(seller)){
            throw new SellerException("seller not found",HttpStatus.NOT_FOUND);
        }

        if(!seller.isActive()){
            logger.error("seller not active ");
            throw new SellerException("seller not active",HttpStatus.FORBIDDEN);
        }
        if (!forgotPasswordDto.getPassword().equals(forgotPasswordDto.getConfirmPassword())) {
            logger.error("password and confirm password doesn't match for user ");
            throw new SellerException("Password and confirm Password doesn't Match",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(!isTokenExpire(token)){
            throw new GenericException("token is expired for this user or the user " +
                    "has logged out ,please login again",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("calling helper function");
        seller.setPassword(passwordEncoder.encode(forgotPasswordDto.getPassword()));
        sellerRepository.save(seller);

        logger.info("deleting previous token");
        tokenRepo.deleteByEmail(email);// for deleting previous token
        emailSender.SimpleEmail(seller.getEmail(), "Password Updated", "your Password Has been Updated" +
                "successfully");

        return new GenerateResponse("password Updated");

    }

    @Override
    public GenerateResponse upDateAddress(String token, Long id,
                                           SellerAddressUpdateDto sellerAddressUpdateDto) {
        logger.warn("validating token");
        boolean isValid=jwtGenerator.validateToken(token);
        if(!isValid){
            throw new GenericException("Token is old or expired",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(!addressRepository.existsById(id)){
            logger.error("no address exists with this id");
            throw new SellerException("Address Doesn't exist for this address Id",
                    HttpStatus.NOT_FOUND);
        }
        Address address=addressRepository.findById(id).orElse(null);

       if(address.getCustomer()!=null && address.getCustomer().getId()!=null){
           throw new SellerException("address id is not of seller",HttpStatus.UNAUTHORIZED);
       }
       String email=jwtGenerator.getUserNameFromJwt(token);
        Seller seller=sellerRepository.findByEmail(email).orElse(null);
        if(Objects.isNull(seller)){
            throw new SellerException("seller not found",HttpStatus.NOT_FOUND);
        }
        if(!seller.isActive()){
            logger.error("seller is not active");
            throw new SellerException("User not Activate",HttpStatus.FORBIDDEN);
        }


        logger.info("updating address");
        if(seller.getId()!=address.getSeller().getId()){
            throw new SellerException("token and id is not from  same user ",
                    HttpStatus.UNAUTHORIZED);
        }
        setAddressHelper(address,sellerAddressUpdateDto);
        address.setSeller(seller);
        logger.info("saving updated address");
        addressRepository.save(address);
        return new GenerateResponse("Address upDated successfully");
    }

    private void setAddressHelper(Address address, SellerAddressUpdateDto sellerAddressUpdateDto) {

        if(sellerAddressUpdateDto.getAddressLine()!=null)
        address.setAddressLine(sellerAddressUpdateDto.getAddressLine());
        if(sellerAddressUpdateDto.getCity()!=null)
        address.setCity(sellerAddressUpdateDto.getCity());
        if(sellerAddressUpdateDto.getLabel()!=null)
        address.setLabel(sellerAddressUpdateDto.getLabel());
        if(sellerAddressUpdateDto.getState()!=null)
        address.setState(sellerAddressUpdateDto.getState());
        if (sellerAddressUpdateDto.getCountry()!=null)
        address.setCountry(sellerAddressUpdateDto.getCountry());
    }

    @Override
    public ResponseEntity<?> imageUpload(String token , MultipartFile file) throws IOException {
        Boolean isValid= jwtGenerator.validateToken(token);
        if(!isValid){
            return new ResponseEntity<>(new GenerateResponse("Invalid Token"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String email=jwtGenerator.getUserNameFromJwt(token);
        Seller user= sellerRepository.findByEmail(email).orElse(null);
        if(user==null){
            return new ResponseEntity<>(new GenerateResponse("No user found"),HttpStatus.NOT_FOUND);
        }
        if(!isTokenExpire(token)){
            return new ResponseEntity<>(new GenerateResponse("token is expired for this user or the user " +
                    "has logged out ,please login again"),
                    HttpStatus.UNAUTHORIZED);
        }
        user.setImagePath(base_path+file.getOriginalFilename());
        userRepository.save(user);
        file.transferTo(new File(base_path+file.getOriginalFilename()));
        return new ResponseEntity(new GenerateResponse("Image uploaded Successfully"),HttpStatus.OK);
    }


    public boolean isTokenExpire(String token){
        if(tokenRepo.existsByToken(token)){
            Tokens tokens=tokenRepo.findByToken(token).orElse(null);
            if(tokens.getExpiration().before(new Date()))return false;
            else
                return  true;
        }
        return false;
    }
}
