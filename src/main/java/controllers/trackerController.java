package com.oyo.feedback.controllers;

import com.oyo.feedback.model.dto.TrackerCountResponse;
import com.oyo.feedback.model.enums.Label;
import com.oyo.feedback.model.enums.Status;
import com.oyo.feedback.services.FeedBackTrackerService;
import com.oyo.feedback.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping( value = "", produces = MediaType.APPLICATION_JSON_VALUE)
public class TrackerController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedBackTrackerService feedBackTrackerService;

    @RequestMapping(value = "/change_status", method = RequestMethod.POST)
    public void changeStatus(@RequestParam(value = "id") Integer feedBackId ,
                             @RequestParam(value = "status",required = false, defaultValue = "COMPLETED") String status)
            throws Exception {
        feedbackService.changeStatus(feedBackId, Status.valueOf(status));
    }

    @RequestMapping(value = "/get_count", method = RequestMethod.GET)
    public Map<String, Map<Label,TrackerCountResponse>> getCount(@RequestParam(value = "hotel_id") String hotelId){
        return feedBackTrackerService.getTrackerCount(hotelId);
    }
}
