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
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.gson.Gson;
import com.google.sps.data.Student;
import com.google.sps.data.TimeRange;
import com.google.sps.data.Tutor;
import com.google.sps.data.TutorSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles user's registration info */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        
        String userId = Optional.ofNullable(request.getParameter("user-id")).orElse(null);
        
        // Find out whether the user is a student or a tutor
        Query query = new Query("User").setFilter(new Query.FilterPredicate("userId", Query.FilterOperator.EQUAL, userId));
        PreparedQuery results = datastore.prepare(query);
        Entity entity = results.asSingleEntity();
        String role = (String) entity.getProperty("role"); 

        // User is a student, get their info
        if (role.toLowerCase().equals("student")) {
            query = new Query("Student").setFilter(new Query.FilterPredicate("userId", Query.FilterOperator.EQUAL, userId));
            results = datastore.prepare(query);
            entity = results.asSingleEntity();

            String name = (String) entity.getProperty("name");
            String bio = (String) entity.getProperty("bio");
            String pfp = (String) entity.getProperty("pfp");
            String email = (String) entity.getProperty("email");
            ArrayList<String> learning = (ArrayList) entity.getProperty("learning");
            ArrayList<TutorSession> scheduledSessions = null;

            Student student = new Student(name, bio, pfp, email, learning, scheduledSessions);

            String json = new Gson().toJson(student);
            response.getWriter().println(json);
        } else {   // User is a tutor, get their info
            query = new Query("Tutor").setFilter(new Query.FilterPredicate("userId", Query.FilterOperator.EQUAL, userId));
            results = datastore.prepare(query);
            entity = results.asSingleEntity();

            String name = (String) entity.getProperty("name");
            String bio = (String) entity.getProperty("bio");
            String pfp = (String) entity.getProperty("pfp");
            String email = (String) entity.getProperty("email");
            ArrayList<String> skills = (ArrayList) entity.getProperty("topics");
            // Will be using availability datastore interface to get these once it's merged
            ArrayList<TimeRange> availability = null;
            ArrayList<TutorSession> scheduledSessions = null;

            Tutor tutor = new Tutor(name, bio, pfp, email, skills, availability, scheduledSessions);

            String json = new Gson().toJson(tutor);
            response.getWriter().println(json);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }
}