package org.gbif.chunked;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Strings;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.ApacheHttpClient4Handler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.eclipse.jetty.server.Server;

/**
 *
 */
public class ChunkedRequester extends TimerTask {
  private final WebResource resource;

  private ChunkedRequester(Client client, int vport) {
    resource = client.resource("http://localhost:"+ vport +"/hello");
    System.out.println("POSTing to url: " + resource.getURI().toString());
  }

  public static ChunkedRequester buildDefaultJerseyClient(int vport) {
    ClientConfig cc = new DefaultClientConfig();
    cc.getClasses().add(JacksonJsonContextResolver.class);
    cc.getClasses().add(JacksonJsonProvider.class);
    cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

    Client c = Client.create(cc);
    c.setFollowRedirects(true);
//    c.addFilter(new LoggingFilter());
    return new ChunkedRequester(c, vport);
  }

  public static ChunkedRequester buildApacheJerseyClient(int port) {
    ApacheHttpClient4Handler hch = new ApacheHttpClient4Handler(new DefaultHttpClient(), null, false);
    ClientConfig clientConfig = new DefaultClientConfig();
    clientConfig.getClasses().add(JacksonJsonContextResolver.class);
    // this line is critical! Note that this is the jersey version of this class name!
    clientConfig.getClasses().add(JacksonJsonProvider.class);
    clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    Client c = new ApacheHttpClient4(hch, clientConfig);
    return new ChunkedRequester(c, port);
  }

  private void post(Object obj) {
    String resp = resource.type(MediaType.APPLICATION_JSON).post(String.class, obj);
    System.out.println("\n" + Strings.repeat("-", 40));
    System.out.println(resp);
  }

  public void run() {
    post(new Hello("Varnish", new Date(), "bla ", 100));
  }

  /**
   * @param args first argument is port of varnish, optional second one the port of the server
   */
  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.out.println("Please supply the local varnish port as first parameter. You can specify the backend port with an optional second parameter, defaulting to 8080");
      System.exit(1);
    }
    int port = 8080;
    if (args.length > 1) {
      System.out.println("Run local server on port " + port);
      port = Integer.valueOf(args[1]);
    }


    Server server = new Server(port);
    server.setHandler(new RequestLogHandler());
    server.start();

    // And From your main() method or any other method
    Timer timer = new Timer();

    TimerTask taskChunked = buildApacheJerseyClient(Integer.valueOf(args[0]));
    timer.scheduleAtFixedRate(taskChunked, 0, 2000);

    TimerTask taskNonChunked = buildDefaultJerseyClient(Integer.valueOf(args[0]));
    timer.scheduleAtFixedRate(taskNonChunked, 500, 2000);

    server.join();
  }
}
