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

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import com.google.gson.Gson;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginStatusServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    String isLoggedIn = "";

    List<String> statusMessages = new ArrayList<>();
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail();
      String urlToRedirectToAfterUserLogsOut = "/login";
      String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
      isLoggedIn = "true";

    //   response.setContentType("text/html");
    //   response.getWriter().println("<p>Hello " + userEmail + "!</p>");
    //   response.getWriter().println("<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>");

      statusMessages.add(isLoggedIn);
      statusMessages.add("Hello " + userEmail + "! \n Logout: " + logoutUrl);
      response.sendRedirect("/index.html");
    } else {
      String urlToRedirectToAfterUserLogsIn = "/login";
      String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
      isLoggedIn = "false";

      statusMessages.add(isLoggedIn);
      statusMessages.add("Login: " + loginUrl);
    //   response.setContentType("text/html");
    //   response.getWriter().println("<p>Hello stranger.</p>");
    //   response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");

    //   response.sendRedirect("/login");
    }

    response.setContentType("application/json;");
    String statusJson = new Gson().toJson(statusMessages);
    response.getWriter().println(statusJson);
  }
}
