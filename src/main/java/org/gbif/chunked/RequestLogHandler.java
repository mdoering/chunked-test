package org.gbif.chunked;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.io.CharStreams;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 *
 */
public class RequestLogHandler extends AbstractHandler {
  public void handle(String target, Request baseRequest, HttpServletRequest req, HttpServletResponse resp)
    throws IOException, ServletException {
    resp.setContentType("text/plain;charset=utf-8");
    baseRequest.setHandled(true);

    PrintWriter out = resp.getWriter();
    out.println(req.getMethod() + ": " + req.getRequestURI());

    out.println("Headers:");
    Enumeration<String> headerNames = req.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      Enumeration<String> values = req.getHeaders(headerName);
      while (values.hasMoreElements()) {
        String val = values.nextElement();
        out.println("  " + headerName + ": " + val);
      }
    }

    out.println("ContentType: " + req.getContentType());
    out.println("ContentLength: " + req.getContentLength());
    out.println("ContentEncoding: " + req.getCharacterEncoding());
    out.println("Content:");

    InputStream in = req.getInputStream();
    final InputStreamReader isr = new InputStreamReader(in, req.getCharacterEncoding() == null ? "UTF8" : req.getCharacterEncoding());
    String text = CharStreams.toString(isr);
    out.println(text);
    in.close();

    resp.setStatus(HttpServletResponse.SC_OK);
  }
}
