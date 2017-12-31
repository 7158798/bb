package com.ruizton.main.tld;

//import javax.servlet.jsp.JspException;
//import javax.servlet.jsp.JspWriter;
//import javax.servlet.jsp.PageContext;
//import javax.servlet.jsp.tagext.TagSupport;
//import java.io.IOException;
//
///**
// * 比特家
// * CopyRight : www.btc58.cc
// * Author : xxp
// * Date： 2016/6/8
// */
//public class PaginationTag extends TagSupport {
//    // 每页显示页码数量
//    private short pageSzie;
//    // 总记录数量
//    private int total;
//    // 当前页码
//    private int pageNow;
//    // 跳转链接
//    private String href;
//
//    // 将此字符串替换为页码
//    private static String PAGE_NUMBER_TAG = "#pageNumber";
//    // 每页页码数量
//    private static int COUNT = 9;
//
//    public void setPageSzie(short pageSzie) {
//        this.pageSzie = pageSzie;
//    }
//
//    public void setTotal(int total) {
//        this.total = total;
//    }
//
//    public void setPageNow(int pageNow) {
//        this.pageNow = pageNow;
//    }
//
//    public int getPageCount(){
//        return (int)Math.ceil(this.total * 1.0 / this.pageSzie);
//    }
//
//    public PageContext getPageContext() {
//        return pageContext;
//    }
//
//    public String getHref() {
//        return href;
//    }
//
//    public void setHref(String href) {
//        this.href = href;
//    }
//
//    @Override
//    public int doEndTag() throws JspException {
//        int pageCount = this.getPageCount();
//        if(0 == pageCount){
//            return TagSupport.EVAL_PAGE;
//        }
//
//        int pageStart = 1;
//        int pageEnd = COUNT;
//
//        if(pageNow > COUNT / 2){
//            pageStart = pageNow - COUNT / 2;
//            pageEnd = pageNow + COUNT / 2;
//        }
//        if(pageEnd > pageCount){
//            pageEnd = pageCount;
//        }
//
//        StringBuilder pageBuf = new StringBuilder();
//        pageBuf.append("<div class=\"page\"><ul>");
//        if(pageNow > 1){
//            pageBuf.append("<li><a href=\"").append(href.replace(PAGE_NUMBER_TAG, "1")).append("\" data-num=\"1\">&lt;</a></li>");
//        }
//        for(int i=pageStart; i<= pageEnd; i++){
//            pageBuf.append("<li><a href=\"").append(href.replace(PAGE_NUMBER_TAG, "" + i)).append("\"").append(" data-num=\"").append(i).append("\"");
//            if(i == pageNow){
//                pageBuf.append(" class=\"current_ss\"");
//            }
//            pageBuf.append(">").append(i + "").append("</a></li>");
//        }
//        if(pageNow < pageCount){
//            pageBuf.append("<li><a href=\"").append(href.replace(PAGE_NUMBER_TAG, "" + pageCount)).append("\" data-num=\"").append(pageCount).append("\">&gt;</a></li>");
//        }
//        pageBuf.append("</div></ul>");
//
//        JspWriter writer = this.getPageContext().getOut();
//        try {
//            writer.write(pageBuf.toString());
//            writer.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return TagSupport.EVAL_PAGE;
//    }
//}
