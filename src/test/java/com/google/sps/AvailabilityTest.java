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

import java.util.List;
import java.util.Arrays;
import java.util.Calendar;
import java.io.StringWriter;
import java.io.PrintWriter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.mockito.Mockito.*;
import com.google.sps.data.TimeRange;
import com.google.sps.data.SampleData;
import com.google.sps.data.TutorSession;
import com.google.sps.servlets.AvailabilityServlet;
import com.google.gson.Gson;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;

@RunWith(JUnit4.class)
public final class AvailabilityTest {
    private static final int TIME_1200AM = TimeRange.getTimeInMinutes(12, 00);
    private static final int TIME_0100PM = TimeRange.getTimeInMinutes(13, 00);
    private static final int TIME_0300PM = TimeRange.getTimeInMinutes(15, 00);
    private static final int TIME_0500PM = TimeRange.getTimeInMinutes(17, 00);
    private static final Calendar MAY182020 = new Calendar.Builder()
                                                        .setCalendarType("iso8601")
                                                        .setDate(2020, 4, 18)
                                                        .set(Calendar.HOUR_OF_DAY, 9)
                                                        .build();
    private static final Calendar AUGUST102020 = new Calendar.Builder()
                                                        .setCalendarType("iso8601")
                                                        .setDate(2020, 7, 10)
                                                        .build();
    private AvailabilityServlet servlet;

    @Before
    public void setUp() {		        
        servlet = new AvailabilityServlet(true);
        TutorSession.resetIds();
    }
  
    @Test
    public void testDoPost() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("tutorID")).thenReturn("kashisharora@google.com");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        when(request.getContentType()).thenReturn("application/json");

        servlet.doPost(request, response);

        String expected = new Gson().toJson(Arrays.asList(TimeRange.fromStartToEnd(TIME_1200AM, TIME_0100PM, MAY182020),
                                                    TimeRange.fromStartToEnd(TIME_0300PM,TIME_0500PM, AUGUST102020)));

        verify(request, times(1)).getParameter("tutorID");
        writer.flush();
        System.out.println(stringWriter.toString());
        System.out.println(expected);
        Assert.assertTrue(stringWriter.toString().contains(expected));
    }
}
