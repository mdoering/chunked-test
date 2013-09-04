package org.gbif.ws.servlet;

import java.util.Date;

public class Greeting {
  private String name;
  private Date date;
  private String location;

  public Greeting() {

  }

  public Greeting(String name, Date date, String location) {
    this.name = name;
    this.date = date;
    this.location = location;
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

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
