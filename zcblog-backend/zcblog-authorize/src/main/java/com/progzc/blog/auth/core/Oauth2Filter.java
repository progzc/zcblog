package com.progzc.blog.auth.core;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.progzc.blog.common.Result;
import com.progzc.blog.common.enums.ErrorEnum;
import com.progzc.blog.common.utils.HttpContextUtils;
import com.progzc.blog.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description 自定义Shiro过滤器
 * @Author zhaochao
 * @Date 2020/11/11 22:57
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
public class Oauth2Filter extends AuthenticatingFilter {

    /**
     * 获取认证令牌
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        String token = getRequestToken((HttpServletRequest) servletRequest);
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return new Oauth2Token(token);
    }

    /**
     * 从请求头中获取token
     * @param httpRequest
     * @return
     */
    private String getRequestToken(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("token");
        // 若请求头中token不存在，则从请求参数中获取token
        if (StringUtils.isEmpty(token)) {
            token = httpRequest.getParameter("token");
        }
        return token;
    }

    /**
     * 放行OPTIONS请求
     * @param servletRequest
     * @param servletResponse
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) {
        // POST请求属于HTTP请求中的复杂请求，HTTP协议在浏览器中对复杂请求会先发起一次OPTIONS的预请求，发起OPTIONS请求常会报403错误
        // 针对这种情况，通常是在DispacerServlet中没有找都到执行OPTIONS请求的方法。
        // 在做跨域处理时，通常配置好跨域请求头信息后，常常忽略在Spring MVC中添加对OPTIONS请求的处理。
        // 解决办法有三种：
        // （1）在Filter中添加对OPTIONS请求的支持处理；（需要搞清楚Filter过滤器和Interceptor拦截器的区别）
        // （2）在Interceptor中添加对OPTIONS请求的支持处理；
        // （3）添加一个支持OPTIONS的ReqeuestMapping（即在控制器中对OPTIONS请求做处理）
        // 本项目采用的是第一种解决方案
        if (((HttpServletRequest) servletRequest).getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        return false;
    }

    /**
     * 提交登录操作
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        String token = getRequestToken((HttpServletRequest) servletRequest);
        // 若token不存在，直接返回401
        if (StringUtils.isEmpty(token)) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true"); // 允许在跨域响应中携带cookie
            httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin()); // 允许跨域响应
            log.debug(ErrorEnum.INVALID_TOKEN.getMsg());
            String resultJson = JsonUtils.toJson(Result.error(ErrorEnum.INVALID_TOKEN));
            httpResponse.getWriter().print(resultJson); // 错误信息输出到页面
            return false;
        }
        return executeLogin(servletRequest, servletResponse); // 若token存在，则执行登录
    }

    /**
     * 登录失败后的操作
     * @param authenticationToken
     * @param e
     * @param servletRequest
     * @param servletResponse
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken authenticationToken, AuthenticationException e, ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletResponse httpResponnse = (HttpServletResponse) servletResponse;
        httpResponnse.setContentType("application/json;charset=utf-8");
        httpResponnse.setHeader("Access-Control-Allow-Credentials", "true"); // 允许在跨域响应中携带cookie
        httpResponnse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        String resultJson = JsonUtils.toJson(Result.error(ErrorEnum.NO_AUTH.getCode(), throwable.getMessage()));
        try {
            httpResponnse.getWriter().print(resultJson);
            log.debug("登录失败");
        } catch (IOException ioException) {
            ioException.printStackTrace();
            log.error(ErrorEnum.UNKNOWN.getMsg());
        }
        return false;
    }

}
