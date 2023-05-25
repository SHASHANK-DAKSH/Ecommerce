package com.example.MainProject.service.dataservice;

import com.example.MainProject.dto.*;
import com.example.MainProject.entities.token.Tokens;
import com.example.MainProject.entities.users.Address;
import com.example.MainProject.entities.users.Customer;
import com.example.MainProject.entities.users.Seller;
import com.example.MainProject.entities.users.User;
import com.example.MainProject.exception.CustomerException;
import com.example.MainProject.exception.GenericException;
import com.example.MainProject.exception.SellerException;
import com.example.MainProject.others.EmailSender;
import com.example.MainProject.repository.*;
import com.example.MainProject.security.JwtGenerator;
import com.example.MainProject.service.dataservice.Impl.CustomerDataServiceImpl;
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
import java.util.*;


@Service
public class CustomerDataService implements CustomerDataServiceImpl {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    JwtGenerator jwtGenerator;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailSender emailSender;

    @Autowired
    TokenRepo tokenRepo;

    @Autowired
    AddressRepository addressRepository;

    Logger logger= LoggerFactory.getLogger(CustomerDataService.class);

    String base_path="/home/shashank/Shashank_Daksh_6980/MainProject/src/" +
            "main/java/com/example/MainProject/Images/customerImage/";
    @Override
    public GetCustomerResponseDto  getCustomer(String token) {
        boolean isValid=jwtGenerator.validateToken(token);
        if (isValid) {
            String email = jwtGenerator.getUserNameFromJwt(token);
            Customer user = customerRepository.findByEmail(email).orElse(null);

            if(Objects.isNull(user)){
                throw new CustomerException("token in header is not of customer",HttpStatus.NOT_FOUND);
            }
            if(user.isActive()) {
                logger.info("user is Active so getting details");
                GetCustomerResponseDto getCustomerResponseDto = new GetCustomerResponseDto();
                logger.info("calling helper function");
                getCustomerHelp(getCustomerResponseDto,user);
                return  getCustomerResponseDto ;
            }
        }
        logger.info("user is not active ,hence no details to be shown");
        throw new CustomerException("user not Active ", HttpStatus.INTERNAL_SERVER_ERROR);

    }
    public void getCustomerHelp(GetCustomerResponseDto getCustomerResponseDto,User user){
        getCustomerResponseDto.setId(user.getId());
        getCustomerResponseDto.setActive(user.isActive());
        getCustomerResponseDto.setFirstName(user.getFirstName());
        getCustomerResponseDto.setLastName(user.getLastname());
        getCustomerResponseDto.setContact(user.getEmail());
        getCustomerResponseDto.setImage(user.getImagePath());

    }
    @Override
    public GenerateResponse updateCustomer(String token, CustomerUpdateDto customerUpdateDto) {
        logger.info("validating token?");
        boolean isValid=jwtGenerator.validateToken(token);
        if (isValid) {
            String email = jwtGenerator.getUserNameFromJwt(token);
            Customer user = customerRepository.findByEmail(email).orElse(null);

            if (Objects.isNull(user)) {// not required but applied inorder to remove warning
                logger.warn("use is null");
                throw new CustomerException("user is null,token in header is not of customer", HttpStatus.NOT_FOUND);
            }

            if(!user.isActive()){
                 throw new CustomerException("user not Active",HttpStatus.INTERNAL_SERVER_ERROR);
            }


            logger.info("calling helper function");
            setCustomerHelper(user,customerUpdateDto);
            userRepository.save(user);

            logger.info("fields updated returning control to controllers");
            return new GenerateResponse("fields Updated");
        }
        throw new CustomerException("Invalid Token", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public void setCustomerHelper(Customer user,CustomerUpdateDto customerUpdateDto){

        if (customerUpdateDto.getFirstName() != null) {
            user.setFirstName(customerUpdateDto.getFirstName().trim());
        }
        if (customerUpdateDto.getPhoneNumber() != null) {
            user.setContact(customerUpdateDto.getPhoneNumber());
        }
        if (customerUpdateDto.getLastName() != null) {
            user.setLastname(customerUpdateDto.getLastName().trim());
        }
    }

    @Override
    public GenerateResponse updateCustomerPassword(String token, ForgotPasswordDto forgotPasswordDto) {
        logger.info("validating token?");
        boolean isValid=jwtGenerator.validateToken(token);
        if (isValid) {
            String email = jwtGenerator.getUserNameFromJwt(token);
            Customer user=customerRepository.findByEmail(email).orElse(null);

            if (Objects.isNull(user)) {
                logger.warn("user is null");
                throw new CustomerException("user is null token in header is not of customer", HttpStatus.NOT_FOUND);
            }
            if(!user.isActive()){
                throw new CustomerException("user not Active",HttpStatus.FORBIDDEN);
            }
            if (!forgotPasswordDto.getPassword().equals(forgotPasswordDto.getConfirmPassword())) {
                logger.warn("checking if password and confirm password same");
                throw new CustomerException("Password and confirm Password doesn't Match",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            user.setPassword(passwordEncoder.encode(forgotPasswordDto.getPassword()));
            customerRepository.save(user);

            emailSender.SimpleEmail(user.getEmail(), "Password Updated", "your Password Has been Updated" +
                    "successfully");

            logger.info("password updated");
            return new GenerateResponse("password Updated");

        }
        throw new CustomerException("Invalid Token", HttpStatus.BAD_REQUEST);
    }

    @Override
    public GenerateResponse addAddress(String token, AddressDto addressDto) {
        logger.info("validating token?");
        boolean isValid= jwtGenerator.validateToken(token);
        if(! isValid){
            throw new CustomerException("not valid token",HttpStatus.FORBIDDEN);
        }
        String email=jwtGenerator.getUserNameFromJwt(token);
        Customer customer = customerRepository.findByEmail(email).orElse(null);
        if(Objects.isNull(customer)){
            throw new CustomerException("no customer found, token in header is not of customer",HttpStatus.NOT_FOUND);
        }
        Address address = new Address();
        addAddressHelper(address,addressDto,customer);
        logger.info("address added successfully");
        return new GenerateResponse("address added");
    }
    public void addAddressHelper(Address address,AddressDto addressDto,Customer customer){
        address.setAddressLine(addressDto.getAddressLine());
        address.setCity(addressDto.getCity());
        address.setLabel(addressDto.getLabel());
        address.setState(addressDto.getState());
        address.setCountry(addressDto.getCountry());
        address.setZipCode(addressDto.getZipCode());
        address.setAddressLine(addressDto.getAddressLine());
        customer.getAddressList().add(address);
        address.setCustomer(customer);
        customerRepository.save(customer);
    }

    @Override
    public List<Address> viewAddress(String token) {

        logger.warn("validating token");
        Tokens tokens=tokenRepo.findByToken(token).orElse(null);
        if(tokens==null){
           throw new CustomerException("Invalid Token",HttpStatus.NOT_FOUND );
        }
        if(tokens.getExpiration().before(new Date())){
            throw new CustomerException("token is Expired",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String email=jwtGenerator.getUserNameFromJwt(token);
        logger.info("getting customer address List");
        Customer customer=customerRepository.findByEmail(email).orElse(null);
        if(Objects.isNull(customer)){
            throw new CustomerException("Token in header is not of customer",HttpStatus.NOT_FOUND);
        }
        List<Address>getAddress=customer.getAddressList();
        return getAddress;

    }

    @Override
    public GenerateResponse deleteAddress(String token, Long id) {
        logger.warn("checking token validation");
        Tokens tokens=tokenRepo.findByToken(token).orElse(null);
        if(tokens==null){
            throw new CustomerException("Invalid Token",HttpStatus.NOT_FOUND);
        }
        if(tokens.getExpiration().before(new Date())){
            throw new CustomerException("token is Expired",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Address address=addressRepository.findById(id).orElse(null);

        if(address.getSeller()!=null&&address.getSeller().getId()!=null){
            throw new SellerException("Address Id is not of customer",HttpStatus.UNAUTHORIZED);
        }
        String email=jwtGenerator.getUserNameFromJwt(token);
        Customer customer=customerRepository.findByEmail(email).orElse(null);
        if(Objects.isNull(customer)){
            throw new CustomerException("Token is not of customer",HttpStatus.NOT_FOUND);
        }
        if(!addressRepository.existsById(id)){
            throw new CustomerException("address doesn't exist by this id",
                    HttpStatus.NOT_FOUND);
        }

        logger.info("checking if address id and customer id is for same user");

        if(address.getCustomer().getId() != customer.getId()){
            throw new CustomerException("token and id is not from  same user ",
                    HttpStatus.UNAUTHORIZED);
        }

        addressRepository.deleteById(id);
        logger.info("field deleted");
        return new GenerateResponse("Address deleted ");
    }

    @Override
    public GenerateResponse updateAddress
            (String token, SellerAddressUpdateDto sellerAddressUpdateDto,Long id) {
        logger.warn("checking token validation");
        Boolean isValid=jwtGenerator.validateToken(token);
          if(!isValid){
            throw new CustomerException("not Valid token",HttpStatus.INTERNAL_SERVER_ERROR);
          }
          logger.info("checking if id exist is Db");
          if(!addressRepository.existsById(id)){
              throw new CustomerException("No value with this Id",HttpStatus.NOT_FOUND);
          }
          Address address=addressRepository.findById(id).orElse(null);

        if(address.getSeller()!=null&&address.getSeller().getId()!=null){
            throw new SellerException("Address Id is not of customer",HttpStatus.UNAUTHORIZED);
        }
         String email=jwtGenerator.getUserNameFromJwt(token);
        Customer customer=customerRepository.findByEmail(email).orElse(null);

        if(Objects.isNull(customer)){
            throw new CustomerException("token in header is not of customer",HttpStatus.NOT_FOUND);
        }

        logger.warn("checking if id and customer id is of same user");
        if(address.getCustomer().getId() != customer.getId()){
            throw new CustomerException("token and id is not from  same user ",
                    HttpStatus.UNAUTHORIZED);
        }
        logger.info("updating address");
        setAddressHelper(address,sellerAddressUpdateDto);
        address.setCustomer(customer);
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
    public GenerateResponse imageUpload(String token , MultipartFile file) throws IOException {

        boolean isValid=jwtGenerator.validateToken(token);
        if(!isValid){
            throw new GenericException("token not Valid",HttpStatus.NOT_FOUND);
        }
        String email=jwtGenerator.getUserNameFromJwt(token);
        Customer user =customerRepository.findByEmail(email).orElse(null);

        if(user==null){
            throw new CustomerException("No user found",HttpStatus.NOT_FOUND);
        }
        user.setImagePath(base_path+file.getOriginalFilename());
        userRepository.save(user);
        file.transferTo(new File(base_path+file.getOriginalFilename()));
        return new GenerateResponse("Image uploaded Successfully");
    }
}

