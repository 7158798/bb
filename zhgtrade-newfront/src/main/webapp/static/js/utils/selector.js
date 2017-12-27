(function($, window, document){
    var defaults = {
        boxClass : "",
        defValue : "-1",
        defItemTitle : "全部",
        initedTriggerChange : false,
        onChange : function(){console.log($(this).val());}
    }

    var curOpenedId = '';

    var methods = {
        init : function(){
            var options = $.extend({}, defaults, arguments[0]);
            return this.each(function(){
                for(var method in methods.initMethods){
                    methods.initMethods[method].call(this, options);
                }
            });
        },
        initSelect : function(opts){
            var $ui = $(this);
            var $curItem = $ui.find(".selector_items a.cur");
            if(0 == $curItem.length){
                $curItem = $ui.find(".selector_items a:eq(0)");
                $curItem.addClass("cur");
            }
            var $input = $ui.find(".selector_item input");
            if($curItem.length > 0){
                var curVal = $curItem.data("val");
                var curTitle = $curItem.html();
                $ui.find(".selector_item span").html(curTitle);
                $input.val(curVal);
            }else{
                $ui.find(".selector_item span").html(opts.defItemTitle);
                $input.val(opts.defValue);
            }
        },
        triggerChange : function(opts){
            var $input = this.find(".selector_items .selector_item input");
            opts.onChange.call($input, $input);
        },
        selected : function(opts){
            var $ui = $(this);
            var $curItem = $ui.find(".selector_items a.cur");
            var $input = $ui.find(".selector_item input");
            var curVal = $curItem.data("val");
            var curTitle = $curItem.html();
            var oldVal = $input.val();
            if($curItem.length > 0){
                $ui.find(".selector_item span").html(curTitle);
                $input.val(curVal);
            }

            if(true === $ui.data("opened")){
                $ui.data("opened", false);
                $ui.find(".selector_items")[0].style.display = 'none';
            }

            if(oldVal != curVal){
                if(true === $ui.data("inited")){
                    opts.onChange.call($input, $input);
                }
            }
        },
        bodyEvent : function(){
            $("body").on("click", function(){
                $(".selector").each(function(){
                    var $this = $(this);
                    if(true === $this.data("opened")){
                        $this.data("opened", false);
                        $this.find(".selector_items")[0].style.display = 'none';
                    }
                });
            });
        },
        initMethods : {
            initUI : function(opts){
                var $ui = $(this);
                if(opts.boxClass){
                    $ui.addClass(opts.boxClass);
                }

                $ui.data("id", new Date());
                $ui.data("inited", true);

                var $curItem = $ui.find(".selector_items a.cur");
                methods.initSelect.call($ui, opts);

                if(opts.initedTriggerChange){
                    methods.triggerChange.call($ui, opts);
                }
            },
            bindEvent : function(opts){
                var $ui = $(this);
                $ui.off("click").on("click", function(){
                    var $this = $(this);
                    var opened = $this.data("opened");
                    if(true === opened){
                        $this.data("opened", false);
                        $this.find(".selector_items")[0].style.display = 'none';
                    }else{
                        curOpenedId = $this.data("id");
                        $this.data("opened", true);
                        $this.find(".selector_items")[0].style.display = 'block';
                    }
                    $(".selector").each(function(){
                        var $this = $(this);
                        if(curOpenedId !== $this.data("id") && true === $this.data("opened")){
                            $this.data("opened", false);
                            $this.find(".selector_items")[0].style.display = 'none';
                        }
                    });
                    return false;
                });

                $ui.find(".selector_items a").off("click").on("click", function(){
                    var $this = $(this);
                    $ui.find(".selector_items a.cur").removeClass("cur");
                    $this.addClass("cur");
                    methods.selected.call($ui, opts);
                    return false;
                });
            }
        }
    }

    $.fn.custom_selector = function(opts){
        var method = arguments[0];

        if(methods[method]) {
            method = methods[method];
            arguments = Array.prototype.slice.call(arguments, 1);
        } else if( typeof(method) == 'object' || !method ) {
            method = methods.init;
        } else {
            return this;
        }

        return method.apply(this, arguments);
    }

    $(function(){
        methods.bodyEvent();
    });
})(jQuery, window, document);


