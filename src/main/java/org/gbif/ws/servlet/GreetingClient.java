package org.gbif.ws.servlet;

import java.util.Date;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.ApacheHttpClientHandler;
import org.apache.commons.httpclient.HttpClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

public class GreetingClient {

  private Client provideDefaultJerseyClient() {
    ClientConfig cc = new DefaultClientConfig();
    cc.getClasses().add(JacksonJsonContextResolver.class);
    cc.getClasses().add(JacksonJsonProvider.class);
    cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

    Client c = Client.create(cc);
    c.setFollowRedirects(true);
    c.addFilter(new LoggingFilter());
    //c.addFilter(new GZIPContentEncodingFilter());
    return c;
  }

  private Client provideApache31JerseyClient() {
    ClientConfig clientConfig = new DefaultClientConfig();
    clientConfig.getClasses().add(JacksonJsonContextResolver.class);
    // this line is critical! Note that this is the jersey version of this class name!
    clientConfig.getClasses().add(JacksonJsonProvider.class);
    clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    ApacheHttpClientHandler handler = new ApacheHttpClientHandler(new HttpClient(), clientConfig);
    Client c = new ApacheHttpClient(handler);
    return c;
  }

  private void post(Object obj) {
    String resp = provideApache31JerseyClient()
      .resource("http://localhost:8080/occurrence/download/request")
      .type(MediaType.APPLICATION_JSON)
      .post(String.class, obj);
    System.out.println(resp);
  }


  public static void main(String[] args) {
    GreetingClient cl = new GreetingClient();
    cl.post(new Greeting("Jimi", new Date(), "Berlin"));
  }
}
