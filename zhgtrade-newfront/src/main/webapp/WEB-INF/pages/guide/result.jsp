<!-- 搜索结果 author:yujie 2016-04-25 -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<%@ taglib uri="/WEB-INF/tlds/activity.tld" prefix="yj" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>${fns:getProperty('site_title')}</title>
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta content=always name=referrer>
    <meta name='renderer' content='webkit' />
    <meta name="keywords" content="${fns:getProperty('site_keywords')}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <link rel="icon" href="/favicon.ico"/>
    <link rel="shortcut icon" href="/favicon.ico"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/common/animate.css"/>
</head>
<body>
<c:set var="menu_index" value="6"/>
<%@include file="../common/header.jsp" %>
<link rel="stylesheet" href="${resources}/static/css/guide.css"/>
<%--<div class="center_page guide_wrapper f14">--%>
<div class="center_page">
    <div class="guide_nav">
        <a href="/" class="f12 c_blue">首页</a>
        <i class="yjt">&gt;</i>
        <a href="/guide/help.html" class="f12 c_blue">新手指南</a>
        <i class="yjt">&gt;</i>
        <a class="c_gray">搜索结果</a>
    </div>
    <%@ include file="../common/guide_left.jsp" %>
    <div class="guide_right fl" data-keyword="${keyword}" id="right">
        <div class="content" style="margin-top: 1.8em">
            <c:choose>
                <c:when test="${list!=null&&list.size()>0}">
                <c:forEach items="${list}" var="article">
                <div class="search_result">
                    <a href="/guide/article.html?id=${article.fid}" class="db ellipsis a_title" title="${article.ftitle}">${article.ftitle}</a>
                    <p>
                        <span class="c_gray">${yj:formatDate(article.flastModifyDate,"yyyy年MM月dd日")}</span>
                        <span class="pl5 new_content"></span>
                    </p>
                    <div class="dn old_content">${article.fcontent}</div>
                </div>
                </c:forEach>
                </c:when>
                <c:otherwise>
                <div class="tac">
                    暂无搜索结果
                </div>
                </c:otherwise>
            </c:choose>
        </div>
        <div style="float:right;padding-bottom:15px;">
            <page:pagination pageSzie="${pageSize}" pageNow="${currentPage}" total="${total}" href="/guide/result?currentPage=#pageNumber&keyword=${keyword}"/>
        </div>
    </div>
</div>
<div class="cb"></div>
<script>
    $(function(){
        function zhuanyi(str){
            var length=str.length;
            if(length!=0){
                var result="";
                for(var i=0;i<length;i++){
                   if("$^-.+*{[]( )}}".indexOf(str[i])!=-1){
                       result+="\\"+str[i];
                   }else{
                       result+=str[i];
                   }
                }
                return result;
            }else{
                return str;
            }
        }
        $("input[name='keyword']").val($("#right").data("keyword"));
        //var beginTime=new Date().getTime();
        $(".search_result").each(function(){
            var $this=$(this);
            var old_keyword=(($("#right").data("keyword"))+"").replace(/\s+/g," ").replace(/^\s|\s$/g,"");
            var keyword=zhuanyi(old_keyword);
            var arr=old_keyword.split(" ");
            var brr=keyword.split("\\ ");
            for(var i=0;i<arr.length;i++){
                var reg=new RegExp(brr[i],'g');
                if(i==0){
                    var title=$this.find(".a_title").text();
                }else{
                    var title=$this.find(".a_title").html();
                }
                //console.log(title);
                var new_title=title.replace(reg,"<span class='c_red'>"+arr[i]+"</span>");
                //console.log(reg);
                //console.log(new_title);
                $this.find(".a_title").html(new_title);
                /*内容*/
                if(i==0){
                    var text=$this.find(".old_content").text();
                    var old_text=text.replace(/\s/g,"");
                    var pos=old_text.indexOf(arr[i]);
                    if(pos!=-1){
                        //console.log(1);
                        $this.find(".new_content").data("flag",1);
                        var new_text=old_text.substr(pos,120)+"...";
                        //console.log(new_text);
                        var str=new_text.replace(reg,"<span class='c_red'>"+arr[i]+"</span>");
                        //console.log(str);
                        $this.find(".new_content").html(str);
                    }else{
                        //console.log(2);
                        //console.log(old_text);
                        $this.find(".new_content").html(old_text.substr(0,120)+"...");
                    }
                }else{
                    var flag=$this.find(".new_content").data("flag");
                    if(flag!=1){//没有关键字
                        var text=$this.find(".old_content").text();
                        var old_text=text.replace(/\s/g,"");
                        var pos=old_text.indexOf(arr[i]);
                        if(pos!=-1){
                            //console.log(3);
                            $this.find(".new_content").data("flag",1);
                            var new_text=old_text.substr(pos,120)+"...";
                            //console.log(new_text+"---");
                            var str=new_text.replace(reg,"<span class='c_red'>"+arr[i]+"</span>");
                            //console.log(str.length);
                            //console.log(str);
                            $this.find(".new_content").html(str);
                        }else{
                            //console.log(4);
                            $this.find(".new_content").html(old_text.substr(0,120)+"...");
                        }
                    }else{//已有关键字
                        var text=$this.find(".new_content").html();
                        var pos=text.indexOf(arr[i]);
                        if(pos!=-1){
                            //console.log(5);
                            $this.find(".new_content").data("flag",1);
                            var str=text.replace(reg,"<span class='c_red'>"+arr[i]+"</span>");
                            $this.find(".new_content").html(str);
                        }else{
                            //console.log(6);
                            $this.find(".new_content").html(text);
                        }
                    }
                }
            }
            //console.log("--------")
            $this.find(".old_content").remove();
            //console.log("每段消耗时间"+(new Date().getTime()-beginTime));
        });
        var  endTime=new Date().getTime();
        //console.log("总共消耗时间"+(endTime-beginTime));
    });
</script>
<%--</div>--%>
<%@ include file="../common/footer.jsp" %>
</body>
</html>

