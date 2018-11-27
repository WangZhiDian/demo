package cn.model.maven.filter;


import javax.servlet.*;
import java.io.IOException;

public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println(" start in filter  " + filterConfig.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("doFilter执行了");
        filterChain.doFilter(servletRequest, servletResponse);    //放行
        System.out.println("执行完返回到客户端");

    }

    @Override
    public void destroy() {

        System.out.println("destory filter ");
    }
}
