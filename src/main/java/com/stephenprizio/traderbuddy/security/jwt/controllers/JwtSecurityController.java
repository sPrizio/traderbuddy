package com.stephenprizio.traderbuddy.security.jwt.controllers;

import com.stephenprizio.traderbuddy.converters.security.UserDTOConverter;
import com.stephenprizio.traderbuddy.models.dto.security.UserDTO;
import com.stephenprizio.traderbuddy.models.records.json.StandardJsonResponse;
import com.stephenprizio.traderbuddy.security.jwt.authentication.JwtTokenManager;
import com.stephenprizio.traderbuddy.security.jwt.models.JwtRequestModel;
import com.stephenprizio.traderbuddy.security.jwt.models.JwtResponseModel;
import com.stephenprizio.traderbuddy.services.security.TraderBuddyUserDetailsService;
import com.stephenprizio.traderbuddy.services.security.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Controller for handling logging in to the app
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@CrossOrigin
@RestController
public class JwtSecurityController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource(name = "jwtTokenManager")
    private JwtTokenManager jwtTokenManager;

    @Resource(name = "traderBuddyUserDetailsService")
    private TraderBuddyUserDetailsService userDetailsService;

    @Resource(name = "userDTOConverter")
    private UserDTOConverter userDTOConverter;

    @Resource(name = "userService")
    private UserService userService;


    //  METHODS

    /**
     * Handles logging a user in to the app
     *
     * @param request {@link JwtRequestModel}
     * @return {@link ResponseEntity} containing a {@link JwtResponseModel}
     */
    @PostMapping("/login")
    public StandardJsonResponse createToken(final @RequestBody JwtRequestModel request) throws InterruptedException {

        //  TODO: TEMP

        Thread.sleep(2000);
        /*this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
        final String jwtToken = this.jwtTokenManager.generateJwtToken(userDetails);

        return ResponseEntity.ok(new JwtResponseModel(jwtToken, true, this.userDTOConverter.convert(this.userService.findUser(request.getUsername()).orElse(null))));*/
        return new StandardJsonResponse(true, new JwtResponseModel("test", true, new UserDTO()), StringUtils.EMPTY);
    }
}
