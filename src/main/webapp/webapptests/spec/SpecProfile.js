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

describe("User Profile", function() {
    
    describe("When profile page loads", function() {
        var mockWindow = {location: {href: "http://localhost:8080/profile.html?userID=123", search:"?userID=123"}};
        var actualUserID = getIdParameter(mockWindow);
        var mockUser = {name: "Sam Falberg", bio: "The best bio", pfp: "images/pfp.jpg", 
            email: "sfalberg@google.com", skills: ["math", "physics"]};
        var actualDiv = createProfileDiv(mockUser);

        it("should correctly get the userID parameter from the URL", function() {
            expect(actualUserID).toEqual("123");
        });

        it("should create div for inside the container", function() {
            expect(actualDiv.tagName).toEqual("DIV");
        });

        it("should create and set img element for pfp", function() {
            expect(actualDiv.childNodes[0].tagName).toEqual("IMG");
            expect(actualDiv.childNodes[0].src).toEqual("http://localhost:8080/images/pfp.jpg");
        });

        it("should create and set h3 element for name", function() {
            expect(actualDiv.childNodes[1].tagName).toEqual("H3");
            expect(actualDiv.childNodes[1].innerHTML).toEqual("Sam Falberg");
        });

        it("should create and set p element for bio", function() {
            expect(actualDiv.childNodes[2].tagName).toEqual("P");
            expect(actualDiv.childNodes[2].innerHTML).toEqual("About me: The best bio");
        });

        it("should create and set p element for email", function() {
            expect(actualDiv.childNodes[3].tagName).toEqual("P");
            expect(actualDiv.childNodes[3].innerHTML).toEqual("sfalberg@google.com");
        });

        it("should create and set p element for topics", function() {
            expect(actualDiv.childNodes[4].tagName).toEqual("P");
            expect(actualDiv.childNodes[4].innerHTML).toEqual("I am tutoring in: math,physics" );
        });
    });
});