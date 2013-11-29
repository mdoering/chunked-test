package org.gbif.chunked;

import java.util.Date;

import com.google.common.base.Strings;

public class Hello {
  private String name;
  private Date date;
  private String message;

  public Hello() {

  }

  public Hello(String name, Date date, String singleMsg, int repeat) {
    this.name = name;
    this.date = date;
    this.message = Strings.repeat(singleMsg, repeat);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
