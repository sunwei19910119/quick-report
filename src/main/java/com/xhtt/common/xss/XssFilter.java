package com.xhtt.common.xss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * XSS过滤
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-01 10:20
 */
@Slf4j
public class XssFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
                (HttpServletRequest) request);
        try {
            chain.doFilter(xssRequest, response);
        } catch (MissingServletRequestParameterException e) {
            System.out.println((xssRequest.getRequestURI()));
            throw e;
        }
    }

    @Override
    public void destroy() {
    }

}
