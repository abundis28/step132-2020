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

import com.google.sps.data.Tutor;
import com.google.sps.data.TimeRange;
import com.google.sps.data.TutorSession;
import com.google.sps.data.SampleData;
import com.google.sps.data.Goal;
import com.google.sps.utilities.GoalDatastoreService;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Calendar;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/goal")
public class GoalServlet extends HttpServlet {
    private GoalDatastoreService datastore;

    public void init() {
        datastore = new GoalDatastoreService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String studentID = Optional.ofNullable(request.getParameter("studentID")).orElse("-1");

        if(studentID.equals("-1")) {
            response.setContentType("application/json");
            response.getWriter().println("{\"error\": \"There was an error getting goals.\"}");
            return;
        }

        // Get student's goals
        List<Goal> goals = datastore.getGoalsByStudent(studentID);

        String json = new Gson().toJson(goals);
        response.setContentType("application/json;");
        response.getWriter().println(json);
        return;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Set default value to -1 
        String studentID = Optional.ofNullable(request.getParameter("studentID")).orElse("-1");
        String goal = request.getParameter("goal");

        if(studentID.equals("-1")) {
            response.setContentType("application/json");
            response.getWriter().println("{\"error\": \"There was an error adding goal.\"}");
            return;
        }

        Goal newGoal = new Goal(studentID, goal);

        // Add goal
        datastore.addGoal(newGoal);

        String json = new Gson().toJson(datastore.getGoalsByStudent(studentID));
        response.setContentType("application/json;");
        response.getWriter().println(json);
        response.sendRedirect("/progress.html?studentID=" + studentID);
        return;
    }
}
