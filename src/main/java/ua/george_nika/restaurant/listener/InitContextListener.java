package ua.george_nika.restaurant.listener;

import org.apache.log4j.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(InitContextListener.class);

    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext context = servletContextEvent.getServletContext();
        String contextPath = context.getContextPath();
        context.setAttribute("context", contextPath);

        LOGGER.info("Program has been started");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOGGER.info("Program has been destroyed");
    }
}
