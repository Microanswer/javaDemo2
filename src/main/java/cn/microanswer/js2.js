"use strict";

!(function() {
    // 申明对象
    var baseObj = function(obj) {
        // 定义全局变量
        this.vars = {
            isCando: false,
            userMsg: {
                auth: '',
                tel: ''
            },
            userDevice: {
                UUID: ''
            }
        };
        // 参数标准化
        this.v = obj ? this.standardization(this.vars, obj) : this.vars;
        // 初始化
        this.init();
    };
    // 添加对象
    baseObj.prototype = {
        init: function() {
            this.v['isCando'] = isMobile.CanDo() ? true : false;
        },
        loadIn: function() {
            var $load = $('#J_loading');
            $load.show();
        },
        loadOut: function() {
            var $load = $('#J_loading');
            setTimeout(function() {
                $load.fadeOut(800);
            }, 500);
        },
        /**
         * [getAjax 获取数据]
         * @param  {[string]}   url       [接口地址]
         * @param  {[string]}   type      [接口类型]
         * @param  {[object]}   parameter [接口参数]
         * @param  {[boolean]}  asyn      [是否为异步执行]
         * @param  {Function}   callback  [成功回调函数]
         * @param  {boolean}    wait      [是否等待]
         * @param  {object}     head      [请求头]
         */
        getAjax: function(url, type, parameter, asyn, callback, wait, head) {
            var that = this;
            $.ajax({
                headers: head ? head : {},
                url: url,
                type: type,
                data: parameter,
                dataType: 'json',
                async: asyn,
                beforeSend: function() {
                    if (wait) {
                        that.waiting();
                    }
                },
                success: function(json) {
                    callback(json);
                },
                complete: function() {
                    if (wait) {
                        that.waitOut();
                    }
                },
                error: function() {
                    alerts('网络异常');
                    console.log('接口请求失败，URL:' + url);
                }
            });
        },
        getUserInfo: function() { // 获取用户信息
            var that = this;
            if (!that.sessionStorage('userMsg') && base.v['isCando']) { // 看度无用户信息时，获取用户信息
                console.log('无用户信息');
                if (typeof(app) != 'undefined') { // 获取登录后的数据并存储
                    var userInfo = app.getLoginUser(); // 安卓字符串,ios 对象，未登录返回空字符串?null?
                    console.log(userInfo);
                    if (!!userInfo) { // 登录
                        userInfo = typeof(userInfo) === 'object' ? userInfo : JSON.parse(userInfo);
                        // 存储session
                        var userMsg = {
                            auth: userInfo.auth,
                            tel: userInfo.mobile
                        };
                        that.sessionStorage('userMsg', userMsg);
                        // 存储变量
                        that.v['userMsg'] = userMsg;
                    }
                } else {
                    console.log('app.getLoginUser()不存在');
                }
            } else if (that.sessionStorage('userMsg')) { // 有用户信息时，将session存储至变量
                console.log('有用户信息');
                var userMsg = that.sessionStorage('userMsg');
                if (typeof(app) != 'undefined') { // 获取登录后的数据并存储
                    var userInfo = app.getLoginUser(); // 安卓字符串,ios 对象，未登录返回空字符串?null?
                    if (!!userInfo) { // 登录
                        userInfo = typeof(userInfo) === 'object' ? userInfo : JSON.parse(userInfo);
                        // 存储session
                        var curUserMsg = {
                            auth: userInfo.auth,
                            tel: userInfo.mobile
                        };
                        if (curUserMsg.tel !== '') {
                            that.sessionStorage('userMsg', curUserMsg);
                            that.v['userMsg'] = curUserMsg;
                        } else {
                            that.v['userMsg'] = userMsg;
                        }
                    }
                } else {
                    that.v['userMsg'] = userMsg;
                }
            }
        },
        getDevice: function() { // 获取设备信息
            var that = this;
            if (!that.sessionStorage('userDevice') && base.v['isCando']) { // 看度无设备信息时，获取设备信息
                console.log('无设备信息');
                if (typeof(app) != 'undefined') { // 获取设备信息数据并存储
                    var device = app.getDevice(); // 安卓字符串,ios 对象，未登录返回空字符串?null?
                    device = (typeof(device) === 'object') ? device : JSON.parse(device);
                    // 存储session
                    that.v['userDevice'].UUID = device.UUID;
                    console.log(device);
                    // 存储变量
                    that.sessionStorage('userDevice', that.v['userDevice']);
                } else {
                    console.log('app.getDevice()不存在');
                }
            } else if (that.sessionStorage('userDevice')) {
                that.v['userDevice'] = that.sessionStorage('userDevice');
                console.log(that.sessionStorage('userDevice'));
            }
            // if (typeof(app) !== 'undefined') {
            //   var device = app.getDevice();
            //   device = (typeof(device) === 'object') ? device : JSON.parse(device);
            //   that.v['userDevice'].UUID = device.UUID;
            //   console.log(app);
            //   console.log(device);
            //   console.log(device.UUID);
            // }
        },
        sessionStorage: function(name, data) {
            if (data) {
                var storeData = '';
                storeData = (typeof(data) === 'object') ? JSON.stringify(data) : data;
                return sessionStorage.setItem(name, storeData);
            } else {
                return JSON.parse(sessionStorage.getItem(name));
            }
        },
        /**
         * [tips 信息提示框]
         * @param  {[string]} msg [提示内容]
         */
        tipsMain: function (tit, msg, fun) {
            var tipsWrap = "#J_tipsMain",
                msg = "<p>" + msg + "</p>",
                html = "";
            if ($(tipsWrap).length > 0) {
                $(tipsWrap)
                    .show()
                    .find(".text")
                    .html(msg);
            } else {
                html =
                    '<div class="tips-main" id="J_tipsMain">\
                      <div class="inner w">\
                        <a class="close" href="javascript:void(0);"></a>\
                        <div class="hd">\
                          <h3>' + tit + '</h3>\
              </div>\
              <div class="bd">' + msg +
                    '</div>\
                  </div>\
                </div>';
                $("body").append(html);
            }
            $("body").css("position", "fixed");

            // 隐藏信息提示框
            $(tipsWrap)
                .find(".close")
                .click(function() {
                    $(tipsWrap).remove();
                    fun && $.isFunction(fun) ? fun() : "";
                    $("body").css("position", "relative");
                });
        },
        waiting: function() {
            var $waitingBox = $('#J_waitingBox'),
                html = '';
            if ($waitingBox.length > 0) {
                $waitingBox.show();
            } else {
                html += '<div class="waiting-box" id="J_waitingBox">\
                    <div class="waiting-inner">\
                      <div class="loader-inner ball-spin-fade-loader">\
                        <div></div>\
                        <div></div>\
                        <div></div>\
                        <div></div>\
                        <div></div>\
                        <div></div>\
                        <div></div>\
                        <div></div>\
                      </div>\
                    </div>\
                  </div>';
                $('body').append(html);
            }
        },
        waitOut: function() {
            $('#J_waitingBox').hide();
        },
        /**
         * [getTwoNumber 得到两位数数字]
         * @param  {[string]} number [数字]
         * @return {[string]}        [格式化后的数字]
         */
        getTwoNumber: function(number) {
            if (parseInt(number) < 10) {
                return '0' + number;
            } else {
                return number;
            }
        },
        /**
         * [timeFormat 时间自由格式化]
         * @param  {[string]} time   [秒数]
         * @param  {[string]} format [格式化类型]
         *     支持的模式字母有：
         *     D:天(1+),
         *     h:小时(0-23),
         *     m:分(0-59),
         *     s:秒(0-59),
         * @return {[string]}        [格式化后的时间]
         */
        timeFormat: function(time, format) {
            if (time != "") {
                time = parseInt(time);
                var map = {
                    D: parseInt(time / 60 / 60 / 24, 10), //天
                    h: parseInt((time / 60 / 60) % 24, 10), //时
                    m: parseInt((time / 60) % 60, 10), //分
                    s: parseInt(time % 60, 10) }; //秒
                format = format.replace(/([YMDhmsqS])+/g, function(all, t) {
                    var v = map[t];
                    if (v !== undefined) {
                        if (all.length > 1) {
                            v = "0" + v;
                            v = v.substr(v.length - 2);
                        }
                        return v;
                    } else if (t === "Y") {
                        return (time.getFullYear() + "").substr(4 - all.length);
                    }
                    return all;
                });
                if (parseInt(time / 60 / 60 / 24, 10) == 0) {
                    format = format.slice(-8);
                }
                return format;
            } else {
                return "";
            }
        },
        /**
         * [standardization 将对象Object标准化]
         * @param  {[object]} o [原对象内容]
         * @param  {[object]} n [替换对象内容]
         * @return {[object]}   [标准化后的对象内容]
         */
        standardization: function(o, n) { // n替换进o
            var k = 0,
                h = {};
            for(k in o) {
                h[k] = o[k];
            }
            for(k in n) {
                var type = typeof(h[k]);
                switch(type) {
                    case 'number':
                        h[k] = parseFloat(n[k]);
                        break;
                    default:
                        h[k] = n[k];
                        break;
                }
            }
            return h;
        },
        /**
         * [log 输出内容到控制台]
         * @param  {[string]} val [内容]
         */
        log: function(val) {
            try {
                console.log(val);
            } catch (e) {}
        }
    };
    window.baseObj = baseObj;
})();


"use strict";

!(function () {
    // 申明对象
    var actObj = function (obj) {
        // 定义全局变量
        this.vars = {
            id: '',
            des: '#description',
            curState: '',
            startTime: '',
            downTime: 0,
            curCount: 60,
            prize: [],
            index: '',
            codeId: '',
            inputTel: '',
            condition: "一元",
            actTime: '',
            proUpdateTime: 60, //单位秒
            promotionId: "#promotionId",
            mainWrap: "#J_actMain",
            loadWrap: "#J_loading",
            startWrap: "#J_actStart",
            endWrap: "#J_actEnd",
            infoWrap: "#J_info",
            yzmWrap: '#J_picYzm',
            // 商品信息和活动时间接口
            curStateApi: "/Home/Promotion/detail",
            // 获取手机验证码接口
            getPhoneyzmApi: "/Home/Promotion/sendSms",
            // 发送订阅短信接口
            sendMsgApi: "/Home/Promotion/subscription",
            // 抢券接口
            getCouponApi: "/Home/Code/pull",
        };
        // 参数标准化
        this.v = obj ? base.standardization(this.vars, obj) : this.vars;
        // 初始化
        this.init();
    };

    // 添加对象
    actObj.prototype = {
        /**
         * [init 初始化]
         */
        init: function () {
            var that = this;
            // 获取用户信息，存入session
            base.getDevice();
            base.getUserInfo();
            // 判断活动状态
            that.checkState();
            that.eventFun();
        },
        /**
         * [checkState 判断活动状态]
         */
        checkState: function () {
            var that = this;
            that.getState(function (that) {
                switch (that.v['curState']) {
                    case 0: // 活动未开始
                        that.actBefore();
                        break;
                    case 1: // 活动进行中
                        that.actBegin();
                        break;
                    case 2: // 活动结束
                        that.actEnd();
                        break;
                }
            });
            base.loadOut();
        },
        /**
         * [getState 请求活动奖品与状态接口]
         */
        getState: function (fun) {
            var that = this;
            base.getAjax(that.v['curStateApi'], 'get', {promotion_id: that.v['id']}, true, function (json) {
                if (json.state && json.data) {
                    var data = json.data;
                    that.v['actTime'] = data.activation_time; // 券激活时间
                    that.v['startTime'] = data.start_time; // 活动开始时间
                    that.v['curState'] = data.status; // 当前活动状态
                    that.v['downTime'] = data.remaining_time; // 倒计时
                    that.v['prize'] = data.prize; // 商品信息内容obj
                    fun(that);
                } else {
                    base.log('活动状态接口数据错误！');
                    var $wrap = $(that.v['startWrap']),
                        html = '';
                    html += '<div class="hd">' + json.message + '</div>';
                    $wrap.html(html);
                }
            }, false);
        },
        /**
         * [actBefore 活动未开始]
         */
        actBefore: function () {
            var that = this;
            var times = that.v['downTime'],
                $wrap = $(that.v['startWrap']),
                data = that.v['prize'],
                html = '',
                item = null;
            // 创建HTML
            html += '<div class="hd">';
            html += '<span class="activity-money">' + that.v['condition'] + '</span>';
            html += '领取以下一件奖品<br/>活动开始倒计时:<span id="J_times">' + base.timeFormat(times, "DD天hh:mm:ss") + '</span>';
            html += '</div>';
            html += '<div class="bd award-list">';
            for (var i = 0; i < data.length; i++) {
                item = data[i];
                html += '<div class="award-item item-' + ((i % 4) + 1) + '">';
                html += '<div class="item-pic"><img src="' + item.url + '" alt="' + item.name + '"></div>';
                html += '<div class="item-title">' + item.name + '</div>';
                html += '<div class="item-value">价值:<span>' + Number(item.price).toFixed(2) + '</span>元</div>';
                html += '<a class="item-btn J_getMessaBtn" href="javascript:void(0);">短信通知</a>';
                html += '<div class="item-remain">剩余: <span class="J_itemRemain">' + item.over + '</span></div>';
                html += '</div>';
            }
            html += '</div>';
            $wrap.html(html);
            // 活动开始倒计时
            var timerState = setInterval(function () {
                times--;
                if (times === 0) {
                    clearInterval(timerState);
                    // 重新获取状态
                    that.checkState();
                }
                // 倒计时HTML
                $wrap.find('#J_times').html(base.timeFormat(times, 'DD天hh:mm:ss'));
            }, 1000);
        },
        /**
         * [actBegin 活动进行中]
         */
        actBegin: function () {
            var that = this;
            var times = that.v['downTime'],
                $wrap = $(that.v['startWrap']),
                data = that.v['prize'],
                html = '',
                item = null;
            // 添加HTML
            html += '<div class="hd">';
            html += '<span class="activity-money">' + that.v['condition'] + "</span>";
            html += '领取以下一件奖品';
            html += '</div>';
            html += '<div class="bd award-list" id="J_itemList">';
            for (var i = 0; i < data.length; i++) {
                item = data[i];
                html += '<div class="award-item item-' + ((i % 4) + 1) + '">';
                html += '<div class="item-pic"><img src="' + item.url + '" alt="' + item.name + '"></div>';
                html += '<div class="item-title">' + item.name + "</div>";
                html += '<div class="item-value">价值:<span>' + Number(item.price).toFixed(2) + '</span>元</div>';
                if (item.over === 0) {
                    html += '<span class="item-btn btn-disable" href="javascript:void(0);">已领完</span>';
                } else {
                    html += '<a class="item-btn J_getItemBtn" href="javascript:void(0);">' + item.money_amount + '元领取</a>';
                }
                html += '<div class="item-remain">剩余: <span class="J_itemRemain">' + item.over + "</span></div>";
                html += "</div>";
            }
            html += "</div>";
            $wrap.html(html);
            // 活动结束倒计时
            var timerState = setInterval(function () {
                times--;
                if (times === 0) {
                    clearInterval(timerState);
                    clearInterval(timerItem);
                    that.checkState();
                }
            }, 1000);
            // 剩余商品定时查询
            var timerItem = setInterval(function () {
                that.getState(function (that) {
                    var data = that.v['prize'],
                        item = null;
                    for (var i = 0; i < data.length; i++) {
                        item = data[i];
                        $wrap.find('.award-item:eq(' + i + ') .J_itemRemain').html(item.over);
                        if (item.over === 0) {
                            if ($wrap.find('.award-item:eq(' + i + ') .J_getItemBtn').length > 0) {
                                $wrap.find('.award-item:eq(' + i + ') .J_getItemBtn').remove();
                                if (!$wrap.find('.award-item:eq(' + i + ') .btn-disable').length > 0) {
                                    $wrap.find('.award-item:eq(' + i + ')').append('<span class="item-btn btn-disable" href="javascript:void(0);">已领完</span>');
                                }
                            }
                        } else {
                            if ($wrap.find('.award-item:eq(' + i + ') .btn-disable').length > 0) {
                                $wrap.find('.award-item:eq(' + i + ') .btn-disable').remove();
                                if (!$wrap.find('.award-item:eq(' + i + ') .J_getItemBtn').length > 0) {
                                    $wrap.find('.award-item:eq(' + i + ')').append('<a class="item-btn J_getItemBtn" href="javascript:void(0);">1元领取</a>');
                                }
                            }
                        }
                    }
                });
            }, that.v['proUpdateTime'] * 1000);
        },
        /**
         * [actEnd 活动结束]
         */
        actEnd: function () {
            var that = this;
            var $actwrap = $(that.v['startWrap']),
                $endWrap = $(that.v['endWrap']);
            // 隐藏活动HTML
            $actwrap.empty();
            $actwrap.hide();
            $endWrap.show();
            // 添加结束HTML
            var html = '';
            html += '<div class="end-btn">';
            if (!base.v['isCando']) {
                html += '<a id="J_attention" onclick="openAppUrl();" class="activity-btn J_active J_openAppBtn" href="javascript:void(0);">立即' + openApp.btnText + '看度</a>';
            }
            html += '</div>';
            $endWrap.find('.bd').append(html);
        },
        /**
         * [eventFun 点击事件]
         */
        eventFun: function () {
            var that = this;
            // 活动说明
            $("#J_activityBtn").click(function () {
                base.tipsMain('活动说明', $(that.v['des']).html());
            });

            // 订阅短信-输入信息
            $('body').on('click', '.bd .award-item .J_getMessaBtn', function () {
                if (base.v['userMsg'].tel) {
                    var param = {
                        promotion_id: that.v['id'],
                        auth: base.v['userMsg'].auth,
                        mobile: base.v['userMsg'].tel
                    };
                    that.getMess(param);
                } else {
                    base.loadIn();
                    $(that.v['startWrap']).hide();
                    var html = '';
                    html += '<section class="activity-msg w" id="J_info">';
                    html += '<div class="activity-msg-notice">';
                    html += '<div class="msg-box m-rl">';
                    html += '<div class="title">开抢时间<br><p class="time">' + dateFormat(that.v['startTime'], 'MM月DD日 hh:mm') + '</p></div>';
                    html += '<div class="title cont">输入手机号码，活动开始会收到短信通知</div>';
                    html += '<div class="msg-input mt2 clearfix">';
                    html += '<input id="J_tel" type="num" placeholder="输入手机号码">';
                    html += '<input id="J_yzm" class="short" type="num" placeholder="输入验证码">';
                    html += '<div id="J_yzmBtnBox"><a id="J_yzmBtn" class="activity-btn J_active btn-yzm" href="javascript:void(0);">获取验证码</a></div>';
                    html += '</div>';
                    html += '<div class="end-btn mt4">';
                    html += '<a id="J_pushMsg" class="activity-btn J_active" href="javascript:void(0);">确定</a>';
                    html += '</div></div></div>';
                    html += '</section>';
                    html += '<!-- 开抢提醒-短信通知 end -->';
                    $('body').append(html);
                    $('body').addClass('of');
                    base.loadOut();
                }
            });

            // 一元领取-输入信息
            $('body').on('click', '.bd .award-item .J_getItemBtn', function () {
                var index = $(this).parent('.award-item').index(),
                    data = that.v['prize'],
                    item = data[index];
                that.v['index'] = index;
                that.v['codeId'] = item.code_id;
                if (base.v['userMsg'].tel) {
                    that.v['inputTel'] = base.v['userMsg'].tel;
                    var param = {
                        code_id: that.v.codeId,
                        auth: base.v['userMsg'].auth,
                        mobile: base.v['userMsg'].tel,
                    };
                    var head = {
                        'X-ClientId': base.v['userDevice'].UUID
                    };
                    console.log(head);
                    that.getGoods(param, head);
                } else {
                    base.loadIn();
                    $(that.v['startWrap']).hide();
                    var html = '';
                    html += '<section class="activity-msg w" id="J_info">';
                    html += '  <div class="activity-msg-get" style="display: block;">';
                    html += '    <div class="award-list">';
                    html += '      <div class="award-item item-' + ((index % 4) + 1) + '">';
                    html += '        <div class="item-pic">';
                    html += '          <img src="' + item.url + '" alt="' + item.name + '">';
                    html += '        </div>';
                    html += '        <div class="item-title">' + item.name + '</div>';
                    html += '        <div class="item-value">价值:<span>' + Number(item.price).toFixed(2) + '</span>元</div>';
                    html += '      </div>'
                    html += '    </div>';
                    html += '    <div class="msg-box m-rl">';
                    html += '      <div class="title">填手机号码领取奖品</div>';
                    html += '      <div class="msg-input mt2 clearfix">';
                    html += '        <input id="J_tel" type="num" placeholder="输入手机号码">';
                    html += '        <input id="J_yzm" class="short" type="num" placeholder="输入验证码">';
                    html += '        <div id="J_yzmBtnBox"><a id="J_yzmBtn" class="activity-btn J_active btn-yzm" href="javascript:void(0);">获取验证码</a></div>';
                    html += '      </div>';
                    html += '      <div class="end-btn mt4">';
                    html += '        <a id="J_pushItem" class="activity-btn J_active" href="javascript:void(0);">提交领取</a>';
                    html += '      </div>';
                    html += '    </div>';
                    html += '  </div>';
                    html += '</section>';
                    $('body').append(html);
                    $('body').addClass('of');
                    base.loadOut();
                }

            });

            // 获取图形验证码
            $('body').on('click', '#J_yzmBtn', function () {
                var myTel = $('#J_tel').val();
                if (myTel.length == 11 && (/^1[3456789]\d{9}$/.test(myTel))) {
                    var html = '';
                    html += '<div class="activity-msg-yzm w" id="J_yzmBox">';
                    html += '<div class="sub-yzm">';
                    html += '<p class="text">请输入图片验证码：</p>';
                    html += '<div class="sub-yzm-img">';
                    html += '<a class="clearfix" id="J_picYzmBox">';
                    html += '<img class="picYzm" id="J_picYzm" src="/index.php?g=Public&m=Public&a=verifycode&' + Math.random() + '" alt="点击刷新验证码">';
                    html += '<div class="update"></div>';
                    html += '</a>';
                    html += '</div>';
                    html += '<div class="sub-input sub-yzm-box clearfix">';
                    html += '<div class="ccover"></div>';
                    html += '<div class="myInput myInput1" id="J_Input1"></div>';
                    html += '<div class="myInput myInput2" id="J_Input2"></div>';
                    html += '<div class="myInput myInput3" id="J_Input3"></div>';
                    html += '<div class="myInput myInput4" id="J_Input4"></div>';
                    html += '<input type="text" name="" class="initem" id="J_initem" value="" maxlength="4">';
                    html += '</div>';
                    html += '<a class="close" href="javascript:void(0);"></a>';
                    html += '</div>';
                    html += '</div>';
                    // $(that.v['infoWrap']).append(html);
                    $('body').append(html);
                } else {
                    if (myTel.length == 0) {
                        alerts('请输入手机号');
                    } else {
                        alerts('请输入正确的手机号码');
                    }
                }
            });

            //输入值监听
            $('body').on('input propertychange', '#J_initem', function () {
                var text = $(this).val().trim();
                var len = $(this).val().trim().length;
                if (len < 1) {
                    $("#J_Input1").html("").removeClass("on");
                    $("#J_Input2").html("").removeClass("on");
                    $("#J_Input3").html("").removeClass("on");
                    $("#J_Input4").html("").removeClass("on");
                } else if (len == 1) {
                    $("#J_Input2").html("").removeClass("on");
                    $("#J_Input3").html("").removeClass("on");
                    $("#J_Input4").html("").removeClass("on");
                } else if (len == 2) {
                    $("#J_Input3").html("").removeClass("on");
                    $("#J_Input4").html("").removeClass("on");
                } else if (len == 3) {
                    $("#J_Input4").html("").removeClass("on");
                }
                if (text == "") {
                    $("#J_Input1").html("");
                    $("#J_Input2").html("");
                    $("#J_Input3").html("");
                    $("#J_Input4").html("");
                }
                for (var i = 0; i < len; i++) {
                    $("#J_Input" + (i + 1)).html(text.substring(i, i + 1));
                    $("#J_Input" + (i + 1)).addClass("on");
                }
                if (len == 4) {
                    //验证码输入完成，请求接口
                    var myTel = $('#J_tel').val();
                    var codeFour = $("#J_initem").val().trim();//该值为输入的结果
                    //输入4个数值了，请求接口
                    var params = {
                        mobile: myTel,//手机号码
                        imgCode: codeFour,//验证码
                        promotion_id: $(that.v['promotionId']).val()
                    };
                    base.getAjax(that.v['getPhoneyzmApi'], 'get', params, true, function (json) {
                        if (json.state) {
                            $('#J_yzmBox').remove();
                            alerts('短信发送成功');
                            // 开启计时器
                            $('#J_yzmBtnBox').html('<span id="J_yzmNone" class="activity-btn J_active btn-yzm" href="javascript:void(0);">(60s)</span>');
                            var count = that.v['curCount'];
                            var InterValObj = window.setInterval(function () {
                                if (count === 0) {
                                    window.clearInterval(InterValObj);//停止计时器
                                    $('#J_yzmBtnBox').html('<a id="J_yzmBtn" class="activity-btn J_active btn-yzm" href="javascript:void(0);">获取验证码</a>');
                                    $('#J_yzmBtn').html("重新发送");
                                }
                                else {
                                    count--;
                                    $('#J_yzmNone').html('(' + count + 's)');
                                }
                            }, 1000); //启动计时器，1秒执行一次
                        } else {
                            alerts(json.message);
                            console.log(json.message);
                            $("#J_initem").focus();
                            $("#J_initem").val("");
                            $("#J_Input1").html("").removeClass("on");
                            $("#J_Input2").html("").removeClass("on");
                            $("#J_Input3").html("").removeClass("on");
                            $("#J_Input4").html("").removeClass("on");
                            var yzmPicApi = "/index.php?g=Public&m=Public&a=verifycode&" + Math.random();
                            $(that.v['yzmWrap']).attr('src', yzmPicApi);
                        }
                    }, false);
                }
            });

            //touch事件，触发输入
            $('body').on('click', '#J_yzmBox .sub-input .ccover', function () {
                $("#J_initem").focus();
            });

            // 更新验证码
            $('body').on('click', '#J_picYzmBox', function () {
                console.log($(that.v['yzmWrap']));
                var yzmPicApi = "/index.php?g=Public&m=Public&a=verifycode&" + Math.random();
                $(that.v['yzmWrap']).attr('src', yzmPicApi);
            });

            // 关闭图形验证码盒子
            $('body').on('click', '#J_yzmBox .close', function () {
                $('#J_yzmBox').remove();
            });

            // 提交信息-订阅短信
            $('body').on('click', '#J_pushMsg', function () {
                var myTel = $('#J_tel').val(),
                    myYzm = $('#J_yzm').val();
                if (myTel.length == 11 && (/^1[3456789]\d{9}$/.test(myTel))) {
                    if (myYzm.length == 4 && !isNaN(myYzm)) {
                        // 请求接口
                        var msgParam = {
                            mobile: myTel,//手机号码
                            verifyCode: myYzm,//短信验证码
                            promotion_id: that.v['id'],
                        };
                        that.getMess(msgParam);
                    } else {
                        if (myYzm.length == 0) {
                            alerts('请输入短信验证码');
                        } else {
                            alerts('验证码输入错误');
                        }
                    }
                } else {
                    if (myTel.length == 0) {
                        alerts('请输入手机号');
                    } else {
                        alerts('请输入正确的手机号码');
                    }
                }
            });

            // 提交信息-抢购商品
            $('body').on('click', '#J_pushItem', function () {
                var myTel = $('#J_tel').val(),
                    myYzm = $('#J_yzm').val();
                that.v['inputTel'] = myTel;
                if (myTel.length == 11 && (/^1[3456789]\d{9}$/.test(myTel))) {
                    if (myYzm.length == 4 && !isNaN(myYzm)) {
                        // 请求接口
                        var msgParam = {
                            mobile: myTel,//手机号码
                            verifyCode: myYzm,//短信验证码
                            code_id: that.v.codeId//商品id
                        };
                        var head = {
                            'X-ClientId': base.v['userDevice'].UUID
                        };
                        console.log(head);
                        that.getGoods(msgParam, head);
                    } else {
                        if (myYzm.length == 0) {
                            alerts('请输入短信验证码');
                        } else {
                            alerts('验证码输入错误');
                        }
                    }
                } else {
                    if (myTel.length == 0) {
                        alerts('请输入手机号');
                    } else {
                        alerts('请输入正确的手机号码');
                    }
                }
            });

            // 关闭tips
            $('body').on('click', '#J_closeTips', function () {
                $('#J_tipsMain').remove();
                $('#J_info').remove();
                $('body').removeClass('of');
                $(that.v['startWrap']).show();
                $("body").css("position", "relative");
            });

            // 关闭tips
            $('body').on('click', '#J_close', function () {
                $('#J_tipsMain').remove();
                $("body").css("position", "relative");
            });

            // 跳转我的优惠券
            $('body').on('click', '#J_getTips', function () {
                if (isMobile.CanDo()) {
                    base.getUserInfo();
                    if (base.v['userMsg'].tel && base.v['userMsg'].tel == that.v['inputTel']) {
                        window.location.href = '/Home/Promotion/myTickets';
                    } else {
                        alerts("请使用领券手机号登录看度查看优惠券！", function () {
                            window.location.href = 'http://m.cditv.cn/_Cditv_CanDo_Url_Parse.php?__=cbnvdA%2FmOA1C0pBbnyFaK6fbgis7ZHqGOydy0p9sGFeiUdLHpOoNsoeaknYRfXtS';
                        });
                    }
                } else {
                    tips("请" + openApp.btnText + "看度，查看我的优惠券！");
                }
            });

        },
        // 订阅短信
        getMess: function (param) {
            var that = this;
            // base.loadIn();
            base.getAjax(that.v['sendMsgApi'], 'get', param, true, function (json) {
                if (json.state) {
                    //短信订阅成功
                    var html = '';
                    html += '<div class="tips-pic mt4">';
                    html += '<div class="tips-pic-yes"></div>';
                    html += '</div>';
                    html += '<div class="hint mt2">活动开始10分钟前<br>我们会以短信的形式邀请你参加</div>';
                    html += '<div class="tips-btn mt2"><a id="J_closeTips" class="activity-btn red-btn J_active" href="javascript:void(0);">知道了</a></div>';
                    base.tipsMain('短信设置成功', html);
                } else {
                    //短信订阅失败
                    alerts(json.message);
                }
            }, false);
            // base.loadOut();
        },
        // 抢购商品
        getGoods: function (param, head) {
            var that = this;
            // base.loadIn();
            var head = {
                'X-ClientId': base.v['userDevice'].UUID
            };
            base.getAjax(that.v['getCouponApi'], 'get', param, true, function (json) {
                var index = that.v.index,
                    data = that.v.prize,
                    item = data[index],
                    html = '';
                if (json.state) {
                    //商品领取成功
                    html += '<div class="award-item item-' + ((index % 4) + 1) + '">';
                    html += '  <div class="item-pic">';
                    html += '    <img src="' + item.url + '" alt="' + item.name + '">';
                    html += '  </div>';
                    html += '  <div class="item-title">' + item.name + '</div>';
                    html += '  <div class="item-value">价值:<span>' + Number(item.price).toFixed(2) + '</span>元</div>';
                    html += '</div>';
                    html += '<div class="hint mt2">请在' + that.v['actTime'] + '内到看度APP“我的优惠券”中激活优惠码<br>';
                    if (item.expiry_type == 1) {
                        html += '并在' + dateFormat(item.expiry, 'YYYY年MM月DD日 hh:mm') + '前，';
                    }
                    html += '在各大舞东风超市<span>仅需支付' + item.money_amount + '元</span>即可领走商品';
                    if (item.expiry_type == 2) {
                        html += '(长期有效)';
                    }
                    html += '</div>';
                    html += '<div class="tips-btn mt2"><a id="J_getTips" class="activity-btn red-btn J_active" href="javascript:void(0);">我的优惠券</a></div>';
                    base.tipsMain('恭喜你,领取成功', html);
                } else {
                    console.log(json);
                    console.log(json.code);
                    console.log(json.state);
                    html += '<div class="tips-pic mt4">';
                    html += '  <div class="tips-pic-no"></div>';
                    html += '</div>';
                    if (json.message) {
                        html += '<div class="hint mt2">' + json.message + '</div>';
                    } else {
                        html += '<div class="hint mt2">其他错误</div>';
                    }
                    //手机号已领过-我的优惠券
                    if (json.code === 10002) {
                        html += '<div class="tips-btn mt2"><a id="J_getTips" class="activity-btn red-btn J_active" href="javascript:void(0);">我的优惠券</a></div>';
                        base.tipsMain('您已领取过了', html);
                        //领取失败
                    } else {
                        //手机设备已领过-知道了-首页
                        if (json.code === 10001 && that.v['id']) {
                            html += '<div class="tips-btn mt2"><a class="activity-btn red-btn J_active" href="/Home/Promotion/index?id=' + that.v['id'] + '">知道了</a></div>';
                            //已抢光-重新领取-首页
                        } else if (json.code === 10003 && that.v['id']) {
                            html += '<div class="tips-btn mt2"><a class="activity-btn red-btn J_active" href="/Home/Promotion/index?id=' + that.v['id'] + '">重新领取</a></div>';
                            //验证码错误及其他-知道了-关闭
                        } else {
                            html += '<div class="tips-btn mt2"><a id="J_close" class="activity-btn red-btn J_active" href="javascript:void(0);">知道了</a></div>';
                        }
                        base.tipsMain('非常抱歉', html);
                    }
                }
            }, false, head);
            // base.loadOut();
        }

    };
    window.actObj = actObj;
})();

function repage (val) {
    console.log('回调成功');
    base.v['userMsg'].auth = val.auth;
    base.sessionStorage('userMsg', base.v['userMsg']);
    window.location.href = '/Home/Promotion/myTickets';
}

// openApp.urlKey = "";
var base = new baseObj();
var act = new actObj({
    id: $('#promotionId').val(),
});


/*
 * @Author: omtech.cn
 * @Date: 2018-11-09 17:07:09
 * @Last Modified by: Doris.Lee
 * @Last Modified time: 2018-11-26 14:40:37
 */

!(function (){
    // 申明对象
    var quanObj = function (obj){
        this.vars = {
            id: '',
            // getUser: app.getLoginUser(),
            itemMsg: [],
            couponWrap: '#J_mycoupon',
            // 我的优惠券
            myCouponApi: "/Home/Promotion/myTickets",
            // 激活优惠券
            activeApi: "/Home/Code/activation"
        };
        // 参数标准化
        this.v = obj ? base.standardization(this.vars, obj) : this.vars;
        // 初始化
        this.init();
    };

    // 添加对象
    quanObj.prototype = {
        init: function() {
            var that = this;
            base.getDevice();
            if (base.v['isCando']) {
                base.getUserInfo();
                that.coupon();
                base.loadOut();
                that.eventFun();
            } else {
                window.location.href = '/Home/Promotion/index?id=' + that.v['id'];
                // tips("请" + openApp.btnText + "看度，查看我的优惠券！");
            }
        },
        // 客户端内显示页面
        coupon: function() {
            var that = this,
                $coupon = $(that.v['couponWrap']);
            // 暂时存放用户信息
            var param = {
                auth: base.v['userMsg'].auth
            };
            base.getAjax(that.v['myCouponApi'], 'get', param, true, function(json) {
                var data = json.data,
                    item = null,
                    html = '';

                if (json.state && data.length > 0) {
                    console.log(json.message);
                    that.v['itemMsg'] = data;
                    html += '<div class="award-list pt2">';
                    for (var i = 0; i < data.length; i++) {
                        item = data[i];
                        html += '<div class="award-item item-' + ((i % 4) + 1) + '">';
                        html += '  <div class="item-pic">';
                        html += '    <img src="' + item.url + '" alt="' + item.name + '">';
                        html += '  </div>';
                        html += '  <div class="item-title">' + item.name + '</div>';
                        switch (item.status) {
                            //已领取(显示激活按钮)
                            case 'RECEIVED':
                                html += '<div class="item-tips" id="time' + i + '">激活倒计时：' + base.timeFormat(item.end_activation,"DD天hh:mm:ss") + '</div>';
                                html += '<a class="item-btn J_received" href="javascript:void(0);">激活</a>';
                                break;
                            //已激活(显示打开按钮)
                            case 'ACTIVATED':
                                if (item.validity_period != 0){
                                    html += '<div class="item-tips">使用有效期：' + dateFormat(item.validity_period,'YYYY-MM-DD hh:mm') + '</div>';
                                } else {
                                    html += '<div class="item-tips">(此券长期有效)</div>';
                                }
                                html += '<a class="item-btn J_activated" href="javascript:void(0);">打开</a>';
                                break;
                            //失效
                            case 'INVALID':
                                html += '<span class="item-btn J_invalid btn-disable" href="javascript:void(0);">失效</span>';
                                break;
                            //已使用
                            case 'USED':
                                html += '<span class="item-btn J_used btn-disable" href="javascript:void(0);">已使用</span>';
                                break;
                            //过期
                            case 'EXPIRED':
                                html += '<span class="item-btn J_expired btn-disable" href="javascript:void(0);">过期</span>';
                                break;
                        }
                        html += '  <div class="item-value">价值:<span class="J_itemValue">' + Number(item.price).toFixed(2) + '</span>元</div>';
                        html += '</div>';
                    }
                    html += '</div>';
                    $coupon.html(html);
                    var timer = setInterval(function() {
                        for (var i = 0; i < data.length; i++) {
                            item = data[i];
                            if (item.status == 'RECEIVED') {
                                item.end_activation--;
                                $coupon.find("#time" + i).html('激活倒计时：' + base.timeFormat(item.end_activation,"DD天hh:mm:ss"));
                                if (item.end_activation <= 0) {
                                    $coupon.find("#time" + i).next('.item-btn:first').remove();
                                    $coupon.find("#time" + i).parent().append('<span class="item-btn J_getItemBtn btn-disable" href="javascript:void(0);">失效</span>');
                                    $coupon.find("#time" + i).remove();
                                }
                                // console.log(temp[i].end_activation);
                            }
                        }
                        if ($coupon.find('.J_received').length <= 0) {
                            clearInterval(timer);
                        }
                    }, 1000);
                } else {
                    console.log(json.message);
                    html += '';
                    html += '<div class="award-none">';
                    html += '  <div class="tips-none"></div>';
                    html += '  <div class="tips-btn mt2"><a class="activity-btn red-btn J_active" href="/Home/Promotion/index?id=' + that.v['id'] + '">立即去领取</a></div>';
                    html += '</div>';
                    $coupon.html(html);
                }
            },false);
        },
        // 点击事件
        eventFun: function() {
            var that = this,
                $coupon = $(that.v['couponWrap']);
            // 激活券码
            $('body').on('click', '#J_mycoupon .J_received', function() {
                var index = $(this).parent('.award-item').index();
                var data = that.v['itemMsg'],
                    item = data[index];
                var param = {
                    id: item.id,
                    auth: base.v['userMsg'].auth
                };
                var head = {
                    'X-ClientId': base.v['userDevice'].UUID
                };
                console.log(head);
                // 添加券信息HTML
                base.getAjax(that.v['activeApi'], 'get', param, true, function(json) {
                    base.waiting();
                    if (json.state) {
                        var curItem = json.data;
                        var html = '';
                        html += '<div id="J_actCoupon" class="tips-barcode w">';
                        html += '  <div class="inner">';
                        html += '    <div class="hd">激活成功</div>';
                        html += '    <div class="name mt2">“' + item.name + '”券</div>';
                        html += '    <div class="codeBox mt4">';
                        html += '      <p class="text">提示：购物时将此码出示给舞东风收银员</p>';
                        html += '      <div class="img-box"><img id="J_myCode" data-src="' + curItem.bar_code_url + '" src="' + item.bar_code_url + '" alt="点击加载"></div>';
                        html += '      <div class="code-tic">券码：' + curItem.code + '</div>';
                        html += '      <div class="code-yzm">验证码：' + curItem.verify_code + '</div>';
                        html += '    </div>';
                        html += '    <a class="close mt4 m-auto" href="javascript:void(0);"></a>';
                        html += '  </div>';
                        html += '</div>';
                        $('body').append(html);
                        base.waitOut();
                    } else {
                        alerts(json.message);
                        base.waitOut();
                    }
                },true,head);
                // 关闭券信息，激活按钮变为打开按钮
                $('body').on('click', '#J_actCoupon .close', function() {
                    $('#J_actCoupon').remove();
                    var html = '';
                    if (item.validity_period != 0){
                        html += '<div class="item-tips">使用有效期：' + dateFormat(item.validity_period,'YYYY-MM-DD hh:mm') + '</div>';
                    } else {
                        html += '<div class="item-tips">(此券长期有效)</div>';
                    }
                    html += '<a class="item-btn J_activated" href="javascript:void(0);">打开</a>';
                    $coupon.find("#time" + index).next('.item-btn:first').remove();
                    $coupon.find("#time" + index).parent().append(html);
                    $coupon.find("#time" + index).remove();
                    window.location.reload();
                });
                // 更新券码
                $('body').on('click', '#J_myCode', function() {
                    $(this).attr('src',$(this).attr('data-src')+'&t='+Math.random());
                });
            });
            // 打开券码
            $('body').on('click', '#J_mycoupon .J_activated', function() {
                var index = $(this).parent('.award-item').index();
                var data = that.v['itemMsg'],
                    item = data[index];
                // 添加券信息HTML
                var html = '';
                html += '<div id="J_seeCoupon" class="tips-barcode w">';
                html += '  <div class="inner">';
                html += '    <div class="name mt2">“' + item.name + '”券</div>';
                html += '    <div class="codeBox mt4">';
                html += '      <p class="text">提示：购物时将此码出示给舞东风收银员</p>';
                html += '      <div class="img-box"><img id="J_myCode" data-src="' + item.bar_code_url + '" src="' + item.bar_code_url + '" alt="点击加载"></div>';
                html += '      <div class="code-tic">券码：' + item.code + '</div>';
                html += '      <div class="code-yzm">验证码：' + item.verify_code + '</div>';
                html += '    </div>';
                html += '    <a class="close mt4 m-auto" href="javascript:void(0);"></a>';
                html += '  </div>';
                html += '</div>';
                $('body').append(html);
                // 关闭券信息
                $('body').on('click', '#J_seeCoupon .close', function() {
                    $('#J_seeCoupon').remove();
                });
                // 更新券码
                $('body').on('click', '#J_myCode', function() {
                    console.log(1);
                    $(this).attr('src',$(this).attr('data-src')+'&t='+Math.random());
                });
            });
        }
    };
    window.quanObj = quanObj;

})();