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

package com.google.sps;

import com.google.sps.data.User;
import com.google.sps.data.TimeRange;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import java.lang.String;
import com.google.gson.Gson;
import com.google.sps.utilities.SearchDatastoreService;

/** Servlet that returns a list of users for a searched name. */
@WebServlet("/search-user")
public class SearchUserServlet extends HttpServlet {
    private SearchDatastoreService datastore;

    public void init() {
        datastore = new SearchDatastoreService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");

        response.setContentType("application/json;");

        // Send error message if the search was invalid.
        if (name == null || name.equals("")) {
            response.getWriter().println("{\"error\": \"Invalid search request.\"}");
            return;
        }

        List<User> results = datastore.getUsersForName(name);

        response.setCharacterEncoding("UTF-8");

        String json = new Gson().toJson(results);

        response.getWriter().println(json);
        return;
    }

}
