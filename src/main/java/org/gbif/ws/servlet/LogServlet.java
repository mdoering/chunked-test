package org.gbif.ws.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.io.CharStreams;

public class LogServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    log(req, resp);
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    log(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    log(req, resp);
  }

  private void log(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/plain");
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
