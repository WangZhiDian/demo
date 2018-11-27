package cn.model.maven.listener;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class MyContextListener extends ContextLoader implements ServletContextListener
{

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        super.initWebApplicationContext(servletContextEvent.getServletContext());


    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {
        closeWebApplicationContext(servletContextEvent.getServletContext());
    }

}
