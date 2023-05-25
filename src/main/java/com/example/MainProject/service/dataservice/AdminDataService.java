package com.example.MainProject.service.dataservice;

import com.example.MainProject.Controllers.datacontroller.AdminDataController;
import com.example.MainProject.dto.CustomerResponseDto;
import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.dto.SellerResponseDto;
import com.example.MainProject.entities.users.Customer;
import com.example.MainProject.entities.users.Seller;
import com.example.MainProject.entities.users.User;
import com.example.MainProject.exception.AdminException;
import com.example.MainProject.exception.GenericException;
import com.example.MainProject.others.EmailSender;
import com.example.MainProject.repository.CustomerRepository;
import com.example.MainProject.repository.SellerRepository;
import com.example.MainProject.repository.UserRepository;
import com.example.MainProject.service.dataservice.Impl.AdminDataServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service

public class AdminDataService implements AdminDataServiceImpl {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    EmailSender emailSender;

    @Autowired
    UserRepository userRepository;
    Logger logger= LoggerFactory.getLogger(AdminDataController.class);

    @Override
    public List<CustomerResponseDto> getCustomerData(int pageSize, int pageOffSet, String sortOn){

        logger.warn("checking if pagination param is given correctly or not");
        if(sortOn.equals("email")||sortOn.equals("id")||sortOn.equals("firstName")||sortOn.equals("lastName")) {

            Pageable p1 = PageRequest.of(pageOffSet, pageSize, Sort.Direction.ASC, sortOn);
            Page<Customer> customerData = customerRepository.findAll(p1);
            List<CustomerResponseDto> data = new ArrayList<>();
            logger.info("calling helper function");
            getCustomerHelper(data, customerData);
            return data;
        }
        logger.error("wrong pagination details added");
        throw new GenericException("not a valid sorting filed",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public void getCustomerHelper(List<CustomerResponseDto>data,Page<Customer>customerData){
        for(Customer c:customerData){
            CustomerResponseDto customerResponseDto=new CustomerResponseDto();
            customerResponseDto.setId(c.getId());
            if(c.getMiddleName()==null) {
                customerResponseDto.setName(c.getFirstName() + " " + c.getLastname());
            }else{
                customerResponseDto.setName(c.getFirstName()+" "+c.getMiddleName()+" "+c.getLastname());
            }
            customerResponseDto.setEmail(c.getEmail());
            customerResponseDto.setActive(c.isActive());
            data.add(customerResponseDto);
        }
    }

    @Override
    public  List<SellerResponseDto> getSellerData(int pageSize, int pageOffSet, String sortOn){
        logger.warn("checking if pagination param is given correctly or not");

        if(sortOn.equals("email")||sortOn.equals("id")||sortOn.equals("firstName")||sortOn.equals("lastName")) {

            Pageable p1 = PageRequest.of(pageOffSet, pageSize, Sort.Direction.ASC, sortOn);
            Page<Seller> sellerList = sellerRepository.findAll(p1);
            List<SellerResponseDto> sellerResponseDto = new ArrayList<>();
            logger.info("calling helper function");
            getSellerDataHelp(sellerList, sellerResponseDto);

            return sellerResponseDto;
        }
        logger.error("pagination details are wrong");
        throw new AdminException("enter a valid sorting field",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public void getSellerDataHelp(Page<Seller>sellerList,List<SellerResponseDto>sellerResponseDto){
        for(Seller s:sellerList){
            SellerResponseDto sellerResponseDto1=new SellerResponseDto();
            sellerResponseDto1.setId(s.getId());
            sellerResponseDto1.setEmail(s.getEmail());
            sellerResponseDto1.setFullName(s.getFirstName()+" "+s.getLastname());
            sellerResponseDto1.setActive(s.isActive());
            sellerResponseDto1.setCompanyContact(s.getCompanyContact());
            sellerResponseDto1.setCompanyAddress(s.getAddress());
            sellerResponseDto1.setCompanyName(s.getCompanyName());

            sellerResponseDto.add(sellerResponseDto1);
        }
    }

    @Override
    public GenerateResponse adminActivation(Long id) {
        logger.info("checking is user exist");
        User user=userRepository.findById(id).orElse(null);
        if(Objects.isNull(user)){
            logger.error("user not found");
            throw new AdminException("no user found",HttpStatus.NOT_FOUND);
        }
        logger.info("setting user active");
        if(!user.isActive()){
            user.setActive(true);
            userRepository.save(user);
            emailSender.SimpleEmail(user.getEmail(),"Account activation message",
                    "your Account is activated now");

            return new GenerateResponse("Account activated by Admin");

        }
        else {
            return new GenerateResponse("user already activate");
        }

    }

    @Override
    public GenerateResponse adminDeActivation(Long id) {
        logger.info("checking is user exist");
        User user=userRepository.findById(id).orElse(null);
        if(Objects.isNull(user)){
            logger.error("user not found");
            throw new AdminException("no user found",HttpStatus.NOT_FOUND);
        }
        logger.info("deactivating User");

        if(user.isActive()){
            user.setActive(false);
            userRepository.save(user);
            emailSender.SimpleEmail(user.getEmail(),"Account Deactivation message",
                    "your Account has been DeActivated ");

            return new GenerateResponse("Account DeActivated by Admin");

        }
        else {
            return new GenerateResponse("user already DeActivated");
        }

    }

    public GenerateResponse adminActivation2(Long id,Boolean flag) {
        logger.info("checking is user exist");
        User user=userRepository.findById(id).orElse(null);
        if(Objects.isNull(user)){
            logger.error("user not found");
            throw new AdminException("no user found",HttpStatus.NOT_FOUND);
        }
        logger.info("setting user active");
        if(Objects.isNull(flag)){
            throw new GenericException("flag is null",HttpStatus.UNAUTHORIZED);
        }

        if (flag == user.isActive()) {
            // throw exception
            String message = "Account already "+ (flag? "Activated": "Deactivated");
            // throw exception
        }
            user.setActive(flag);
            userRepository.save(user);
            emailSender.SimpleEmail(user.getEmail(), "Account  message",
                    "your Account is activated now");

            return new GenerateResponse("Account activated by Admin");


    }
}
