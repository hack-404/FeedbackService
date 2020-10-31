package com.oyo.feedback.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE )
public class pingController {

    @RequestMapping(value = "/ping" , method = RequestMethod.GET)
    public ResponseEntity heath(){
        return ResponseEntity.ok().body("Go Corona Corono Go");
    }

}
