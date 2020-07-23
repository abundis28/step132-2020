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

function getLists() {
    return getListsHelper();
}

async function getListsHelper(window) {
    await fetch('/manage-lists', {method: 'GET'}).then((response) => {
        //if the tutor is not the current user/signed in
        if(response.redirected) {
            window.location.href = response.url;
            alert("You must be signed in to manage lists.");
            return [];
        }
        return response.json();
        
    }).then((lists) => {
        if(lists.error) {
            var message = document.createElement("p");
            p.innerText = timeslots.error;
            document.getElementById('list-names').appendChild(message);
            return;
        }

        var listNames = document.getElementById('list-names');
        var listInfo = document.getElementById('lists')

        lists.forEach((list) => {
            listNames.appendChild(createListNameLink(list));
            listInfo.appendChild(createListElement(list));
        });
    });
}

function createListElement(list) {
    var container = document.createElement("div");
    var listName = document.createElement("h3");
    var topic = document.createElement("p");
    var books = document.createElement("ul");

    listName.innerText = list.name;
    topic.innerText = capitalizeFirstLetter(list.topic.toLowerCase());

    list.books.forEach((book) => {
        var elem = document.createElement("li");
        elem.className = "book";
        elem.innerText = book;
        books.appendChild(elem);
    });

    container.id = list.name;

    listName.className = "list-name";
    topic.className = "list-topic";
    books.className = "book-list";
    container.classList.add("col-md-8");
    container.classList.add("list-info");

    container.appendChild(listName);
    container.appendChild(topic);
    container.appendChild(books);

    container.style.display = "none";

    return container;
}

function createListNameLink(list) {
    var listNameLink = document.createElement("a");

    listNameLink.href = "#";

    listNameLink.innerText = list.name;

    listNameLink.classList.add("list-group-item");
    listNameLink.classList.add("list-group-item-action");
    listNameLink.classList.add("list-name-link");

    listNameLink.addEventListener("click", function() {
        switchList(listNameLink);
    });

    return listNameLink;
}

function switchList(elem) {
    //get the name of the project that was clicked
    var listName = elem.innerText;
    //get the current list that we see
    var currentActiveList = document.getElementsByClassName("active-list")[0];
    var currentActiveListLink = document.getElementsByClassName("active")[0];

    if(currentActiveList !== undefined && currentActiveListLink !== undefined) {
        //remove the active/active-list classes from the list that we currently see
        //the active/active-list classes determine whether or not we can see the element
        currentActiveList.classList.remove("active-list");
        currentActiveListLink.classList.remove("active");
    }
    //get the list we clicked on
    var nextActiveList = document.getElementById(listName);
    //add the active/active-list classes to the project we clicked so we can see the description/image
    elem.classList.add("active");
    nextActiveList.classList.add("active-list");
}

//Reference: https://stackoverflow.com/questions/1026069/how-do-i-make-the-first-letter-of-a-string-uppercase-in-javascript
function capitalizeFirstLetter(string) {
  return string.charAt(0).toUpperCase() + string.slice(1);
}