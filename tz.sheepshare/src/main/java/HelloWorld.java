import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.context.ContextLoaderListener;

public class HelloWorld extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        resp.getWriter().print("Hello from Java!/n");
    }

    // http://wiki.eclipse.org/Jetty/Tutorial/Embedding_Jetty
    // https://code.google.com/p/penumbrous/source/browse/trunk/src/java/com/penumbrous/posts/p20110109/EmbeddedJettyExample.java
    public static void main(String[] args) throws Exception{
        String port = System.getenv("PORT");
        if(port == null)
            port = "8001";
        
        WebServer server = new WebServer(Integer.parseInt(port));
        server.start();
        
//        final Server server = new Server(Integer.valueOf(port));

//        WebAppContext context = new WebAppContext();
//        context.setContextPath("/");
//        context.setWar("C:/LHF_IDE/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/tz.sheepshare");
//        server.setHandler(context);

        //        server.setHandler(createWebAppContext2());
//        server.start();
//        server.join();

        //        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        //        context.setContextPath("/tz.sheepshare");
        //
        //        // Setup Spring context
        //        context.addEventListener(new ContextLoaderListener());
        //        context.setInitParameter("contextConfigLocation", "classpath:spring/context-*.xml");
        //        context.addEventListener(new org.springframework.web.util.Log4jConfigListener());
        //        context.addEventListener(new org.springframework.web.context.ContextLoaderListener());
        //        context.addEventListener(new org.springframework.security.web.session.HttpSessionEventPublisher());
        //        //        context.addFilter(CharacterEncodingFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST) );
        //        server.setHandler(context);
        //        // Add servlets
        //        //        context.addServlet(new ServletHolder(new BatchReceiver()), "/receiver/*");
        //        context.addServlet(new ServletHolder(new HelloWorld()), "/*");
        //        server.start();
        //        server.join();
    }

    private static WebAppContext createWebAppContext2(){
        final WebAppContext context = new WebAppContext();
        context.setContextPath("/tz.sheepshare");

        // Setup Spring context
        context.addEventListener(new ContextLoaderListener());
        context.setInitParameter("contextConfigLocation", "classpath:spring/context-*.xml");
        context.addEventListener(new org.springframework.web.util.Log4jConfigListener());
        context.addEventListener(new org.springframework.web.context.ContextLoaderListener());
        context.addEventListener(new org.springframework.security.web.session.HttpSessionEventPublisher());

        context.setBaseResource(new ResourceCollection(new String[] { "./src/main/webapp" })); //./src/main/webapp, ./webapp
        return context;
    }

    // http://pseudelia.wordpress.com/tutorials/hello-world-with-spring-3-2-and-jetty-9-example/
    // http://workingonthecoolstuff.blogspot.kr/2011/03/configuring-jetty-7-programatically.html
    private static WebAppContext createWebAppContext(){
        final WebAppContext context = new WebAppContext();
        context.setContextPath("/tz.sheepshare");
        context.setBaseResource(new ResourceCollection(new String[] { "./src/main/webapp" })); //./src/main/webapp, ./webapp
        return context;
    }
}