package com.zhgtrade.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by sunpeng on 2016/5/20 0020.
 */
@Controller
@RequestMapping("/about")
public class AboutZhgController {

    @RequestMapping("/newsmedia")
    public String newsMedia() {
        return "aboutzhg/newsmedia";
    }

    @RequestMapping("/managerteam")
    public String managerTeam() {
        return "aboutzhg/managerteam";
    }

    @RequestMapping("/events")
    public String events() {
        return "aboutzhg/events";
    }

    @RequestMapping("/contact")
    public String contact() {
        return "aboutzhg/contactus";
    }

}
