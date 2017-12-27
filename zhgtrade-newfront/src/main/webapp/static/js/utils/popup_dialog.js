(function(window, document, $){
    var defaults = {
        level : '', // info warn error
        title : '警告',
        content : '',
        boxClass : '',
        mask : false,
        confirmText : '确认',
        cancelText : '取消',
        confirmBtn : true,
        cancelBtn : false,
        confirmCall : function(){},
        cancelCall : function(){}
    };

    var methods = {
        init : function(){
            var opts = $.extend({}, defaults, arguments[0]);
            methods.render.apply(this, [opts]);
            methods.bindEvent.apply(this, [opts]);

            // 居中
            methods.center.apply($('#' + opts.dialogId), [opts]);
        },
        render: function(){
            var opts = arguments[0];
            var $head = $('head');
            var $body = $(document.body);
            var rndId = Math.ceil(Math.random() * 1000);
            opts.maskId = 'dialog_mask';
            opts.dialogId = 'dialog' + rndId;
            opts.closeBtnId = 'close' + rndId;
            opts.confirmBtnId = 'confirm' + rndId;
            opts.cancelBtnId = 'cancel' + rndId;
            // css
            if(0 == $head.find("#dialog_style").length){
                var style = '<style id="dialog_style">'+
                    '.alert_dialog .dialog_content{width: 270px;}' +
                    '.dialog_mask{position:fixed;top:0px;height:100%;width:100%;left:0px;background:#000;z-index:5;opacity:0.5;filter:alpha(opacity=50);}' +
                    '.dialog_window{max-width:556px;min-width:270px;min-height:90px;position:fixed;z-index:5000;font-size:14px;border-radius:5px;background:#fff;box-shadow:1px 1px 10px #000;-webkit-user-select: none;-moz-user-select: none;-khtml-user-select: none;-ms-user-select: none;-o-user-select: none;user-select: none;}' +
                    '.dialog_window .dialog_title{padding-left:10px;max-width:556px;min-width:270px;height:40px;border-bottom:1px solid #ccc;cursor: move;}' +
                    '.dialog_window .dialog_title span{display:inline-block;height:40px;line-height:40px;font-size:16px;color: #333;text-align: center;}' +
                    '.dialog_window .dialog_title span.warn:after{float:left;margin-top:1px;padding-right:5px;text-align:center;display:inline-block;height:40px;line-height:40px;content: "\\e601"; font-family: iconfont; font-style: normal; font-size: 20px; color: #fe741f;}' +
                    '.dialog_window .dialog_title span.error:after{float:left;margin-top:1px;padding-right:5px;text-align:center;display:inline-block;height:40px;line-height:40px;content: "\\e60b"; font-family: iconfont; font-style: normal; font-size: 20px; color: #F00;}' +
                    '.dialog_window .dialog_title span.info:after{float:left;padding-right:5px;margin-top:-2px;text-align:center;display:inline-block;height:40px;line-height:40px;content: "\\e62d"; font-family: iconfont; font-style: normal; font-size: 20px; color: #08a3d7;}' +
                    '.dialog_window .dialog_title .close{cursor:pointer;}' +
                    '.dialog_window .dialog_title .close:after{float:right;margin-right:10px;height:40px;line-height:40px;content: "\\e62e"; font-family: iconfont; font-style: normal; font-size: 20px; color: #969696;}' +
                    '.dialog_window .dialog_title .close:hover:after{color: #333;}' +
                    '.dialog_window .dialog_content{max-width:536px;min-width:250px;padding: 15px 10px;text-align: center;}' +
                    '.dialog_window .dialog_btns{bottom:10px;margin:0 auto;text-align: center;height: 50px;line-height: 50px;}' +
                    '.dialog_window .dialog_btns a{text-decoration:none;color:#FFF;text-align:center;min-width:80px;padding: 6px 15px;background-color: #08a3d7;border-radius: 4px;-webkit-border-radius: 4px;-moz-border-radius: 4px;}' +
                    '.dialog_window .dialog_btns a:hover{background-color: #30C2FF;}' +
                    '.dialog_window .dialog_btns span{width:20px;display: inline-block;}' +
                    '</style>';
                $head.append(style);
            }
            // mask
            if(opts.mask && 0 == $body.find("#dialog_mask").length){
                $body.append('<div id="' + opts.maskId + '" class="dialog_mask"></div>');
            }
            // dialog
            var html  = '<div id="' + opts.dialogId + '" class="dialog_window ' + opts.boxClass + '" mask="' + opts.mask + '">';
            html     +=     '<div class="dialog_title">';
            /*html     +=         '<i class="' + opts.level + '"></i>';*/
            html     +=         '<span class="' + opts.level + '">' + opts.title + '</span>';
            html     +=         '<i id = "' + opts.closeBtnId + '" class="close"></i>';
            html     +=     '</div>';
            html     +=     '<div class="dialog_content">' + opts.content + '</div>';
            if(opts.confirmBtn || opts.cancelBtn){
                html +=     '<div class="dialog_btns">';
                if(opts.confirmBtn){
                    html +=     '<a id = "' + opts.confirmBtnId + '" href="javascript:void(0);">' + opts.confirmText + '</a>';
                }
                if(opts.confirmBtn && opts.cancelBtn){
                    html +=     '<span></span>';
                }
                if(opts.cancelBtn){
                    html +=     '<a id = "' + opts.cancelBtnId + '" href="javascript:void(0);">' + opts.cancelText + '</a>';
                }
                html +=     '</div>';
            }
            html     +=  '</div>';
            $body.append(html);
        },
        bindEvent : function(){
            var opts = arguments[0];
            var $dialog = $('#' + opts.dialogId);
            var $title = $dialog.find('.dialog_title');
            $dialog.on('selectstart', function(){
                // ie选中
                return false;
            });
            $dialog.find('#' + opts.closeBtnId).off('click').on('click', function(){
                methods.close.apply($dialog, [opts]);
                return false;
            });
            $dialog.find('#' + opts.confirmBtnId).off('click').on('click', function(){
                opts.confirmCall();
                methods.close.apply($dialog, [opts]);
            });
            $dialog.find('#' + opts.cancelBtnId).off('click').on('click', function(){
                opts.cancelCall();
                methods.close.apply($dialog, [opts]);
            });
            $title.on('mousedown', function (event) {
                $title.data('moving', true);
                $title.data('oldX', event.clientX);
                $title.data('oldY', event.clientY);
                $title.data('left', $dialog.css('left').replace('px', '') * 1);
                $title.data('top', $dialog.css('top').replace('px', '') * 1);
            });
            $(document).on('mouseup', function () {
                $title.data('moving', false);
                $title.data('left', $dialog.css('left').replace('px', '') * 1);
                $title.data('top', $dialog.css('top').replace('px', '') * 1);
            });
            $(document).on('mousemove', function (event) {
                if(true === $title.data('moving')){
                    var disX = event.clientX - $title.data('oldX');
                    var disY = event.clientY - $title.data('oldY');
                    $dialog.css({top : $title.data('top') + disY, left : $title.data('left') + disX});
                }
            });
            $(window).resize(function(){
                methods.center.apply($dialog, [opts]);
            });
        },
        center : function(){
            var $dialog = $(this);
            var width = $dialog.width();
            var height = $dialog.height();
            $dialog.css({top : ($(window).height() - height) / 2, left : ($(window).width() - width) / 2});
        },
        close : function(){
            var opts = arguments[0];
            var $dialog = $(this);
            if(1 == $('.dialog_window[mask="true"]').length){
                $('#' + opts.maskId).hide();
            }
            $dialog.hide(300);
            setTimeout(function(){
                $dialog.remove();
            }, 300);
        }
    };

    $.fn.popup_dialog = function(){
        methods.init.apply(this, arguments);
    }
    /*window.alert = function() {
        $(window).popup_dialog({content:'<p style="">' + arguments[0] + '</p>', boxClass : 'alert_dialog', confirmCall : arguments[1], level : 'warn'});
    };*/
    /*window.confirm = function () {
        $(window).popup_dialog({content:'<p style="">' + arguments[0] + '</p>', boxClass : 'alert_dialog', confirmCall : arguments[1], cancelCall : arguments[2], cancelBtn : true, level : 'warn'});
    };*/
    // 全局
    popup_dialog = function(opts){
        $(window).popup_dialog(opts);
    }
})(window, document, jQuery);