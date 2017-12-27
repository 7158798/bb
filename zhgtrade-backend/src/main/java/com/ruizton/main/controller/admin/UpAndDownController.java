package com.ruizton.main.controller.admin;

import com.alibaba.fastjson.JSON;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.UpanddownHelper;
import com.ruizton.main.service.admin.EntrustService;
import com.ruizton.main.service.admin.EntrustlogService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.util.DateUtils;
import com.ruizton.util.StringUtils;
import com.ruizton.util.XlsExport;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.annotations.Parameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sunpeng on 2016/5/30 0030.
 */
@Controller
@RequestMapping("/ssadmin")
public class UpAndDownController {

    @Resource
    private EntrustlogService entrustlogService;
    @Resource
    private VirtualCoinService virtualCoinService;

    @RequestMapping("/upanddownReport")
    public String getUpAndDown(@RequestParam(required = false) String start, @RequestParam(required = false)String end, Map<String, Object> map) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


        if(!StringUtils.hasText(start) || !StringUtils.hasText(end)){
            // 默认一个月
            end = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            start = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd");

        }


        List<Fvirtualcointype> types = virtualCoinService.findAll();
        List<UpanddownHelper> data = new ArrayList<UpanddownHelper>();
        for (Fvirtualcointype fvirtualcointype:types) {
            Date startTime = format.parse(start);
            Date endTime = format.parse(end);
            double result = entrustlogService.getUpAndDown(fvirtualcointype.getFid(), startTime, endTime);
            System.out.println(result);
            data.add(new UpanddownHelper(fvirtualcointype.getFname(), result));

        }

        data.sort(new Comparator<UpanddownHelper>() {
            @Override
            public int compare(UpanddownHelper o1, UpanddownHelper o2) {
                return Double.valueOf(Math.abs(o2.getUpanddown())).compareTo(Math.abs(o1.getUpanddown()));
            }
        });


        map.put("data", data);
        map.put("start", start);
        map.put("end", end);
        return "ssadmin/upanddownReport";
    }

    /**
     * 导出涨跌幅列表
     * @param
     */
    @RequestMapping(value = "/exportupanddown", method = RequestMethod.GET)
    @RequiresPermissions("ssadmin/exportupanddown.html")
    public void exportExcel(HttpServletResponse response, String start, String end){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Fvirtualcointype> types = virtualCoinService.findAll();
        List<UpanddownHelper> data = new ArrayList<UpanddownHelper>();
        for (Fvirtualcointype fvirtualcointype:types) {
            Date startTime = null;
            Date endTime = null;
            try {
                startTime = format.parse(start);
                endTime = format.parse(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            double result = entrustlogService.getUpAndDown(fvirtualcointype.getFid(), startTime, endTime);
            data.add(new UpanddownHelper(fvirtualcointype.getFname(), result));

        }
        data.sort(new Comparator<UpanddownHelper>() {
            @Override
            public int compare(UpanddownHelper o1, UpanddownHelper o2) {
                return Double.valueOf(Math.abs(o2.getUpanddown())).compareTo(Math.abs(o1.getUpanddown()));
            }
        });
        XlsExport xls = new XlsExport();

        // 标题

        xls.createRow(0);
        xls.setCell(0, "虚拟币");
        xls.setCell(1, "交易e");

        // 填入数据
        int row = 1;
        for (UpanddownHelper upanddown:data) {
            xls.createRow(row++);
            xls.setCell(0, upanddown.getName());
            if(upanddown.getUpanddown() != 0){

                DecimalFormat format2 = new DecimalFormat("##.00%");
                String percent = format2.format(Math.abs(upanddown.getUpanddown()));
                xls.setCell(1, percent);
            }else{
                xls.setCell(1, 0);
            }
        }

        try {
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("涨跌", "utf-8") + format.format(new Date()) + ".xls");
            xls.exportXls(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}

