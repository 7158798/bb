<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<html>
<head>
    <title>上传文件</title>
    <script>
        var code = '${code}';
        if('101' === code){
            parent.window.alert('上传的图片不存在');
        }else if('102' === code){
            parent.window.alert('上传的图片不能大于${max}');
        }else if('103' === code){
            parent.window.alert('不能上传非法图片');
        }else if('104' === code){
            parent.window.alert('上传图片失败');
        }else if('200' === code){
            if(parent.window.showImage){
                parent.window.showImage('${cdn}/${img_url}');
            }
            if(parent.window.showImage2){
                parent.window.showImage2('${cdn}/${img_url}', '/${img_url}', '${param.dom_id}');
            }
        }else{
            parent.window.alert('上传图片失败');
        }
    </script>
</head>
<body>

</body>
</html>
