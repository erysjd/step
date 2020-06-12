// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    int numComm = Integer.parseInt(request.getParameter("num-choice"));
    
    PreparedQuery results = datastore.prepare(query);

    //loading comments from Datastore
    List<String> comments = new ArrayList<>();
    List<String> emails = new ArrayList<>();
    for (Entity commentEntity : results.asIterable(FetchOptions.Builder.withLimit(numComm))) {
      String txt = (String) commentEntity.getProperty("text-input");
      String email = (String) commentEntity.getProperty("email");
      comments.add(txt);
      emails.add(email);
    }

    response.setContentType("application/json;");
    String json = new Gson().toJson(comments);
    response.getWriter().println(json);

    // String emailJson = new Gson().toJson(emails);
    // response.getWriter().println(emailJson);

  }
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    String userEmail = userService.getCurrentUser().getEmail();

    String comment = getParameter(request, "text-input", "");
    long timestamp = System.currentTimeMillis();

    //tests if the comment is an empty string
    if (comment.length() == 0){
      response.sendRedirect("/index.html");
      return;
    }

    // stores the comment in datastore
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text-input", comment);
    commentEntity.setProperty("timestamp", timestamp);
    commentEntity.setProperty("email", userEmail);


    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/index.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
  
}

