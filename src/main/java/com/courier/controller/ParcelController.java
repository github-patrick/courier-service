package com.courier.controller;

import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.domain.dtos.ParcelRequestDto;
import com.courier.domain.dtos.ParcelResponseDto;
import com.courier.domain.enums.ParcelStatus;
import com.courier.domain.enums.Priority;
import com.courier.exception.CannotCreateParcelException;
import com.courier.exception.CustomerNotFoundException;
import com.courier.exception.ParcelNotFoundException;
import com.courier.service.CustomerService;
import com.courier.service.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping
public class ParcelController {

    private ParcelService parcelService;
    private CustomerService customerService;

    @Autowired
    public ParcelController(ParcelService parcelService, CustomerService customerService) {
        this.parcelService = parcelService;
        this.customerService = customerService;
    }

    @PostMapping
    @RequestMapping("/api/v1/customers/{customerId}/parcels")
    public ResponseEntity<ParcelResponseDto> createParcel(@RequestBody @Valid ParcelRequestDto parcelRequestDto,
                                                          @PathVariable Long customerId, HttpServletResponse response,
                                                          Principal principal)
            throws CustomerNotFoundException, CannotCreateParcelException, UnknownHostException {


        CustomerResponseDto customerResponseDto = customerService.getCustomer(customerId);

        if (!customerResponseDto.getCourierUser().getEmail().equals(principal.getName())) {
            throw new CannotCreateParcelException("Cannot create parcel for customer " +customerId + " as you are not logged in with that account.");
        }

        ParcelResponseDto parcelResponseDto = parcelService.addParcel(parcelRequestDto,customerResponseDto);

        Cookie cookie = new Cookie("parcel-id", Integer.toString(new Random().nextInt()));
        cookie.setMaxAge(1 * 24 * 60 *60);
        response.addCookie(cookie);


        MultiValueMap<String,String> map = new HttpHeaders();
        map.add("X-Host-IP", Inet4Address.getLocalHost().getHostAddress());
        return new ResponseEntity(parcelResponseDto, map, HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/parcels/{id}")
    public ResponseEntity<ParcelResponseDto> getParcel(@PathVariable String id, Principal principal) throws ParcelNotFoundException,
            CannotCreateParcelException {
        ParcelResponseDto parcelResponseDto = parcelService.getParcel(id);

        if (!principal.getName().equals(parcelResponseDto.getSender().getCourierUser().getEmail())) {
            throw new CannotCreateParcelException("Cannot retrieve parcel for customer as you are not logged in with that account."); }
        return new ResponseEntity(parcelResponseDto, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/api/v1/parcels")
    public ResponseEntity<List<ParcelResponseDto>> getAllParcels(@RequestParam(required = false) ParcelStatus parcelStatus,
                                                                 @RequestParam(required = false) Priority priority) {
        List<ParcelResponseDto> parcelResponseDtoList = parcelService.getAllParcels(parcelStatus, priority);
        return new ResponseEntity(parcelResponseDtoList,HttpStatus.OK);
    }
}
