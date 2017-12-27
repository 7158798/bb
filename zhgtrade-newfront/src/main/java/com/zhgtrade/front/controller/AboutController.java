package com.zhgtrade.front.controller;

import com.ruizton.main.model.Fabout;
import com.ruizton.main.service.admin.SystemArgsService;
import com.ruizton.main.service.front.FrontOthersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by sunpeng on 2016/5/27 0027.
 */
@Controller
public class AboutController {


    @Autowired
    private FrontOthersService frontOthersService ;
    @Autowired
    private SystemArgsService systemArgsService;

    @RequestMapping("/about/index")
    public ModelAndView index(int id) throws Exception{
        ModelAndView modelAndView = new ModelAndView() ;
        Fabout fabout = frontOthersService.findFabout(id) ;
        modelAndView.addObject("fabout", fabout) ;
        modelAndView.setViewName("about/index") ;
        return modelAndView ;
    }

    @RequestMapping("/download/index")
    public ModelAndView download() throws Exception{
        ModelAndView modelAndView = new ModelAndView() ;
        String downurl = systemArgsService.getValue("downurl");
        modelAndView.addObject("downurl", downurl) ;
        modelAndView.setViewName("/about/download") ;
        return modelAndView ;
    }
}
