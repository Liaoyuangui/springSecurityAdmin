var path = Path();


/**
 * 区分本地地址和服务器地址
 * 获取绝对路径
 * @author:lyg
 */
function Path(){
    var curWwwPath = window.document.location.href;//获取当前网址，如：http://localhost/ems/pages/basic.jsp
    /*console.log("curWwwPath"+curWwwPath)
    var pathName=window.document.location.pathname;	//获取主机地址之后的目录，如:/ems/pages/basic.jsp
    var pos=curWwwPath.indexOf(pathName);
    var localhostPath=curWwwPath.substring(0,pos);//获取主机地址，如：http://localhost:8080
    var projectName=pathName.substring(0,pathName.substr(1).indexOf("/")+1);//获取带"/"的项目名，如：/ems, yx
    var basePath=localhostPath+projectName+"/";//获取项目的basePath   http://localhost:8080/ems/
    console.log("basePath"+basePath)*/
    //添加本地访问地址,如要添加本机ip,在local_path数组中添加，否则树形菜单访问不到
    var local_path = new Array();
    local_path[0]="localhost";
    local_path[1]="127.0.0.1";
    for(var i=0; i<local_path.length; i++){
        var path = local_path[i];
        if(curWwwPath.indexOf(path) != -1 ){
            return "/"  ;  //返回本地项目的根
        }
    }
    return "/服务器的根/";
};


//axios 请求添加默认请求头
// Request interceptors for API calls
axios.interceptors.request.use(
    config => {
        config.headers['token'] =  sessionStorage.getItem('token');
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

//响应验证token是否过期
// Response interceptor for API calls
/*axios.interceptors.response.use(
    response => {
        console.log(1)
        return response;
    },
    error => {
        console.log(2)
        console.log("@@@@",error)
        if(error.response.code == 403){
            //登录过期
            this.$message.error(error.data.msg);
            sessionStorage.removeItem('token');
            location.href = "/toLogin"
        };
        if(error.response.code == 401){
           //未授权
            this.$message.error(res.data.msg);
            sessionStorage.removeItem('token');
        }
    }
);*/



//获取ajax请求，增加请求头,携带token
(function ($) {
    //1.得到$.ajax的对象
    var _ajax = $.ajax;
    $.ajax = function (options) {
        //2.每次调用发送ajax请求的时候定义默认的error处理方法
        var fn = {
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                showmsg(XMLHttpRequest.responseText,2,1)
            },
            success: function (data, textStatus) {
            },
            beforeSend: function (XHR) {
            },
            complete: function (XHR, TS) {
            }
        }
        //3.扩展原生的$.ajax方法，返回最新的参数
        var _options = $.extend({}, {
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                fn.error(XMLHttpRequest, textStatus, errorThrown);
            },
            success: function (data, textStatus) {
                fn.success(data, textStatus);
            },
            beforeSend: function (XHR) {
                XHR.setRequestHeader('token', sessionStorage.getItem("token"));//设置请求头Token  sessionStorage.getItem():说明【js中的session】
                fn.beforeSend(XHR);
            },
            complete: function (XHR, TS) {
                fn.complete(XHR, TS);
            }
        }, options);
        //4.将最新的参数传回ajax对象
        _ajax(_options);
    };
})(jQuery);

/***
 * 跳转,路径都必须携带token
 * @param url
 */
function toUrl(url) {
    var user_token = sessionStorage.getItem("token");
    if(url.indexOf("?") > 0){
        location.href = url+"&token="+user_token;
    }
    location.href = url+"?token="+user_token;
}


