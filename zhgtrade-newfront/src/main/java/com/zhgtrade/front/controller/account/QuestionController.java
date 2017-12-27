package com.zhgtrade.front.controller.account;

import com.ruizton.main.Enum.MessageStatusEnum;
import com.ruizton.main.Enum.QuestionStatusEnum;
import com.ruizton.main.Enum.QuestionTypeEnum;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fquestion;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.service.front.FrontQuestionService;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.util.Constants;
import com.ruizton.util.Utils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunpeng on 2016/5/11 0011.
 */
@Controller
@RequestMapping("/account")
public class QuestionController {

    public static final String jsonEncode = "application/json;charset=UTF-8" ;
    @Autowired
    private FrontUserService frontUserService ;
    @Autowired
    private FrontQuestionService frontQuestionService ;
    @Autowired
    private HttpServletRequest request ;

    @RequestMapping("/question")
    public ModelAndView question() throws Exception {
        ModelAndView modelAndView = new ModelAndView() ;
        Fuser fuser = frontUserService.findById(GetSession().getFid()) ;

        List<String> list = new ArrayList<String>() ;
        for(int i = 1; i<= QuestionTypeEnum.OTHERS; i++){
            list.add(QuestionTypeEnum.getEnumString(i)) ;
        }

        modelAndView.addObject("fuser", fuser) ;
        modelAndView.addObject("question_list", list) ;
        modelAndView.setViewName("account/question") ;
        return modelAndView ;
    }


    /**
     * 返回json数据
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/questionJson", produces = {jsonEncode})
    public String questionJson() throws Exception {
        Fuser fuser = frontUserService.findById(GetSession().getFid()) ;
        Map<String, Object> map = new HashMap<String, Object>();
        for(int i = 1; i<= QuestionTypeEnum.OTHERS; i++){
            map.put(i + "", QuestionTypeEnum.getEnumString(i));
        }
        JSONObject jsonObject = new JSONObject();

        JSONObject userObject = new JSONObject();
        userObject.put("name", fuser.getFrealName());
        userObject.put("telephone", fuser.getFtelephone());

        JSONObject questionObject = new JSONObject();
        for (int i = 1; i <= QuestionTypeEnum.OTHERS; i++){
            questionObject.put(i + "", QuestionTypeEnum.getEnumString(i));
        }
        jsonObject.put("userObject", userObject);
        jsonObject.put("questionObject", questionObject);
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/unreadinformation", produces = {jsonEncode})
    public String unReadInformation(){

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fstatus", MessageStatusEnum.NOREAD_VALUE);
        map.put("freceiver.fid", GetSession().getFid());
        int count = frontQuestionService.findFmessageByParamCount(map);
        return count + "";
    }


    @ResponseBody
    @RequestMapping(value = "/questionSubmit", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String submitQuestion(
            @RequestParam(required=false)int questionType,
            @RequestParam(required=false)String desc,
            @RequestParam(required=false)String name,
            @RequestParam(required=false)String phone
    )throws Exception{
        questionType = questionType<QuestionTypeEnum.COIN_RECHARGE?QuestionTypeEnum.COIN_RECHARGE:questionType ;
        questionType = questionType>QuestionTypeEnum.OTHERS?QuestionTypeEnum.OTHERS:questionType ;


        desc = HtmlUtils.htmlEscape(desc) ;
        name = HtmlUtils.htmlEscape(name) ;

        System.out.println(desc + name + phone + questionType);

        int today_count = frontQuestionService.findByTodayQuestionCount() ;
        if(today_count>=50){
            return String.valueOf(-8) ;
        }

        Fuser fuser = frontUserService.findById(GetSession().getFid()) ;
        Fquestion fquestion = new Fquestion() ;
        fquestion.setFuser(fuser) ;
        fquestion.setFcreateTime(Utils.getTimestamp()) ;
        fquestion.setFdesc(desc) ;
        fquestion.setFname(name) ;
        fquestion.setFstatus(QuestionStatusEnum.NOT_SOLVED) ;
        fquestion.setFtelephone(phone) ;
        fquestion.setFtype(questionType) ;
        frontQuestionService.save(fquestion);
        return String.valueOf(0);
    }


    @RequestMapping("/questionColumn")
    public ModelAndView questionColumn(
            @RequestParam(required=false,defaultValue="1")int type,
            @RequestParam(required=false,defaultValue="1")int currentPage
    ) throws Exception {
        ModelAndView modelAndView = new ModelAndView() ;

        Map<String, Object> param = new HashMap<String, Object>() ;
        param.put("fstatus", type) ;
        param.put("fuser.fid", GetSession().getFid()) ;
        int totalCount = this.frontQuestionService.findByParamCount(param) ;
        int totalPage = totalCount/ Constants.QuestionRecordPerPage +(totalCount%Constants.QuestionRecordPerPage==0?0:1) ;
        currentPage = currentPage<1?1:currentPage ;
        currentPage = currentPage>totalPage?totalCount:currentPage ;

        List<Fquestion> list = frontQuestionService.findByParam(param, (currentPage-1)*Constants.QuestionRecordPerPage, Constants.QuestionRecordPerPage, "fcreateTime desc") ;

        String pagin = generatePagin(totalPage, currentPage,"/account/questionlist?type="+type+"&") ;

        modelAndView.addObject("pageSize", Constants.QuestionRecordPerPage);
        modelAndView.addObject("total", totalCount);
        modelAndView.addObject("pageNow", currentPage);
        modelAndView.addObject("pagin", pagin) ;
        modelAndView.addObject("list",list) ;
        modelAndView.addObject("type", type) ;
        modelAndView.addObject("currentPage", currentPage) ;
        modelAndView.setViewName("account/questioncolumn") ;
        return modelAndView ;
    }


    @RequestMapping("/question-list")
    public ModelAndView questionList(
            @RequestParam(required=false,defaultValue="1")int type,
            @RequestParam(required=false,defaultValue="1")int currentPage
    ) throws Exception{

        ModelAndView modelAndView = questionColumn(type, currentPage);
        modelAndView.setViewName("account/question-list");
        return modelAndView;

    }


    @ResponseBody
    @RequestMapping(value = "/questionlist", produces = {jsonEncode})
    public String questionListJson(@RequestParam(required = false, defaultValue = "1") int type, @RequestParam(required = false, defaultValue = "1") int currentPage){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Map<String, Object> param = new HashMap<String, Object>() ;
        param.put("fstatus", type) ;
        param.put("fuser.fid", GetSession().getFid()) ;
        int totalCount = frontQuestionService.findByParamCount(param) ;
        int totalPage = totalCount/ Constants.QuestionRecordPerPage +(totalCount%Constants.QuestionRecordPerPage==0?0:1) ;
        currentPage = currentPage<1?1:currentPage ;
        currentPage = currentPage>totalPage?totalCount:currentPage ;

        List<Fquestion> list = frontQuestionService.findByParam(param, (currentPage-1)*Constants.QuestionRecordPerPage, Constants.QuestionRecordPerPage, "fcreateTime desc") ;

        String pagin = generatePagin(totalPage, currentPage,"/") ;
        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        for (Fquestion question : list) {
            JSONObject jsonQuestion = new JSONObject();
            jsonQuestion.put("fid", question.getFid());
            jsonQuestion.put("ftype_s", question.getFtype_s());
            jsonQuestion.put("fdesc", question.getFdesc());
            String answer = (question.getFanswer() == null || "".equals(question.getFanswer())) ? "无" : question.getFanswer();
            jsonQuestion.put("fanswer", answer);
            jsonQuestion.put("fcreateTime", sdf.format(question.getFcreateTime()));
            jsonQuestion.put("fstatus_s", question.getFstatus_s());
            jsonArray.add(jsonQuestion);
        }
        jsonObject.put("list", jsonArray);
        jsonObject.put("pagin", pagin);
        jsonObject.put("currentPage", currentPage);
        jsonObject.put("type", type);
        return jsonObject.toString();
    }

    /*
     * /question/cancelQuestion.html?qid="+id+"&currentPage type=
     * */
    @RequestMapping("/cancelQuestion")
    public ModelAndView cancelQuestion(
            @RequestParam(required=false,defaultValue="1")int currentPage,
            @RequestParam(required=false,defaultValue="1")int type,
            @RequestParam(required=true)int qid
    ) throws Exception{
        ModelAndView modelAndView = new ModelAndView() ;
        Fquestion fquestion = this.frontQuestionService.findById(qid) ;
        if(fquestion!=null && fquestion.getFuser().getFid()==GetSession().getFid()){
            this.frontQuestionService.delete(fquestion) ;
        }

        modelAndView.setViewName("redirect:/account/questionColumn.html?type="+type+"&currentPage="+currentPage) ;
        return modelAndView ;
    }

    @RequestMapping("/message")
    public ModelAndView message(
            @RequestParam(required=false,defaultValue="1")int type,
            @RequestParam(required=false,defaultValue="1")int currentPage
    ) throws Exception{
        ModelAndView modelAndView = new ModelAndView() ;


        Map<String, Object> param = new HashMap<String, Object>() ;
        param.put("freceiver.fid", GetSession().getFid()) ;
        int totalCount = this.frontQuestionService.findFmessageByParamCount(param) ;

        int totalPage = totalCount/Constants.QuestionRecordPerPage +(totalCount%Constants.QuestionRecordPerPage==0?0:1) ;
        currentPage = currentPage<1?1:currentPage ;
        currentPage = currentPage>totalPage?totalCount:currentPage ;

        List<Fmessage> list = this.frontQuestionService.findFmessageByParam(param, (currentPage-1)*Constants.QuestionRecordPerPage, Constants.QuestionRecordPerPage, "fcreateTime desc") ;

        String pagin = generatePagin(totalPage, currentPage,"/account/message.html?type="+type+"&") ;


        modelAndView.addObject("pageSize", Constants.QuestionRecordPerPage);
        modelAndView.addObject("total", totalCount);
        modelAndView.addObject("pageNow", currentPage);
        modelAndView.addObject("list", list) ;
        modelAndView.addObject("pagin", pagin) ;
        modelAndView.addObject("type", type) ;
        modelAndView.setViewName("account/message") ;
        return modelAndView ;
    }

    @RequestMapping("/message-list")
    public ModelAndView messageList(
            @RequestParam(required=false,defaultValue="1")int type,
            @RequestParam(required=false,defaultValue="1")int currentPage
    ) throws Exception{
        ModelAndView modelAndView = message(type, currentPage);

        modelAndView.setViewName("account/message-list");

        return modelAndView ;
    }


    @ResponseBody
    @RequestMapping(value="/messagedetail",produces={jsonEncode})
    public String messagedetail(
            @RequestParam(required=true)int id
    ) throws Exception{
        JSONObject jsonObject = new JSONObject() ;

        Fmessage fmessage = this.frontQuestionService.findFmessageById(id) ;
        if(fmessage==null || fmessage.getFreceiver().getFid()!=GetSession().getFid()){
            jsonObject.accumulate("result", false) ;
            return jsonObject.toString() ;
        }

        if(fmessage.getFstatus()== MessageStatusEnum.NOREAD_VALUE){
            fmessage.setFstatus(MessageStatusEnum.READ_VALUE) ;
            this.frontQuestionService.updateFmessage(fmessage) ;
        }

        jsonObject.accumulate("result", true) ;
        jsonObject.accumulate("title", fmessage.getFtitle()) ;
        jsonObject.accumulate("content", fmessage.getFcontent()) ;
        return jsonObject.toString() ;
    }

    /**
     * 删除消息
     * @param id
     * @return 删除成功返回true，失败返回false
     */
    @ResponseBody
    @RequestMapping(value = "/deletemessage", produces = {jsonEncode})
    public String deleteMessage(@RequestParam(required = true) int id){
        Fuser user = GetSession();
        boolean result =  frontQuestionService.deleteMessageById(id, user.getFid());
        if(!result){

            System.out.println(id);
        }
        return result + "";
    }

    /**
     * 批量删除消息
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/deletebundlemessage")
    public String deleteBundleMessage(@RequestParam String params) {
        if (params == "") {
            return "data is empty";
        }
        try {

            Fuser fuser = GetSession();
            String[] strings = params.split(",");
            for (String str : strings) {
                int id = Integer.parseInt(str);
                frontQuestionService.deleteMessageById(id, fuser.getFid());
            }
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    /**
     * 标记已读
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/remarkmessage")
    public String remarkMessage(@RequestParam String params) {
        if (params == "") {
            return "data is empty";
        }
        try {
            Fuser fuser = GetSession();
            String[] strings = params.split(",");
            for (String str : strings) {
                int id = Integer.parseInt(str);
                frontQuestionService.updateMessageRead(id, fuser.getFid());
            }
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }



    //获得session中的用户
    public Fuser GetSession(){
        Fuser fuser = null ;
        HttpSession session = getSession() ;
        Object session_user = session.getAttribute("login_user") ;
        if(session_user!=null){
            fuser = (Fuser)session_user ;
        }
        return fuser ;
    }

    public HttpSession getSession(){
        return getRequest().getSession() ;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public String generatePagin(int total,int currentPage,String path){

        if(total<=0){
            return "" ;
        }

        StringBuilder sb = new StringBuilder() ;

        if(currentPage==1){
            sb.append("<li><a style='color:#FFFFFF;' class='current_ss' href='javascript:void(0)'>1</a></li>") ;
        }else{
            sb.append("<li><a href='"+path+"currentPage=1'>&lt</a></li>") ;
            sb.append("<li><a href='"+path+"currentPage=1'>1</a></li>") ;
        }

        if(currentPage==2){
            sb.append("<li><a style='color:#FFFFFF;' class='current_ss' href='javascript:void(0)'>2</a></li>") ;
        }else if(total>=2){
            sb.append("<li><a href='"+path+"currentPage=2'>2</a></li>") ;
        }

        if(currentPage>=7){
            sb.append("<li><a href='javascript:void(0)'>...</a></li>") ;
        }

        //前三页
        int begin = currentPage-3 ;
        begin = begin<=2?3:begin ;
        for(int i=begin;i<currentPage;i++){
            sb.append("<li><a href='"+path+"currentPage="+i+"'>"+i+"</a></li>") ;
        }

        if(currentPage!=1&&currentPage!=2){
            sb.append("<li><a style='color:#FFFFFF;' class='current_ss' href='javascript:void(0)'>"+currentPage+"</a></li>") ;
        }

        //后三页
        begin = currentPage+1;
        begin = begin<=2?3:begin ;
        int end = currentPage+4 ;
        if(currentPage<6){
            int tInt = 6- currentPage ;
            end = end+((tInt>3?3:tInt)) ;
        }
        for(int i=begin;i<end&&i<=total;i++){
            sb.append("<li><a href='"+path+"currentPage="+i+"'>"+i+"</a></li>") ;
        }


        if(total-currentPage==4){
            sb.append("<li><a href='"+path+"currentPage="+total+"'>"+total+"</a></li>") ;
        }else if(total-currentPage>3){
            sb.append("<li><a href='javascript:void(0)'>...</a></li>") ;
        }

        if(total>=11&&total-currentPage>4){
            sb.append("<li><a href='"+path+"currentPage="+total+"'>"+total+"</a></li>") ;
        }

        if(currentPage<total){
            sb.append("<li><a href='"+path+"currentPage="+total+"'>&gt</a></li>") ;
        }

        return sb.toString() ;
    }
}
