package com.oyo.feedback.controllers;

import com.oyo.feedback.model.dto.FeedbackResponse;
import com.oyo.feedback.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/feedback", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(value = "/get" , method = RequestMethod.GET)
    public Map<String, List<FeedbackResponse>> getFeedbackEntity(@RequestParam(value = "hotel_id") String hotelId,
                                                                 @RequestParam(value = "rating", defaultValue = "7",required = false ) Integer rating) {
         return feedbackService.getFeedbackData(hotelId, rating);
    }
}
