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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

/**
 * Adds a random song to the page.
 */
function addRandomSong() {
  const songs =
      ['Watermelon Sugar by Harry Styles', 'A Muse by dvsn', 'Fade Away by Lucky Daye', 'Are You Bored Yet? by Wallows'];

  // Pick a random song.
  const song = songs[Math.floor(Math.random() * songs.length)];

  // Add it to the page.
  const songContainer = document.getElementById('song-container');
  songContainer.innerText = song;
}

//Fetches the comment from the server and adds it to the DOM.
function fetchComments() {
  const numComm = document.getElementById('num-choice').value;
  fetch('/data?num-choice='+numComm).then(response => response.json()).then((comments) => {
    console.log("comment: " + comments);    
    const commListElement = document.getElementById('comment-container');
    document.getElementById('comment-container').innerHTML = "";
    for (i = 0; i < numComm; i++){
        commListElement.appendChild(createListElement(comments[i]));
    }
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

/** Tells the server to delete the comments. */
function deleteComments() {
  const params = new URLSearchParams();
  fetch('/delete-data', {method: 'POST', body: params});
  var myobj = document.getElementById('comment-container');
  myobj.remove();
}

// unhides the comment form until the user logs in
function revealComments() {
    const commForm = document.getElementById("comm-form");
    //fetches login staus
    fetch('/login').then(response => response.json()).then((status) => {
      console.log("status: " + status);
      loginUrl = status[1].substring(7);
      logoutUrl = status[1].substring(8);
      //if the user is logged in show the commForm and if not, then show it
      if (status[0].localeCompare("false") === 0) {
        console.log("url: " + loginUrl);
        commForm.style.visibility = "hidden";
        // Create the login URL
        var a = document.createElement('a');  
        var link = document.createTextNode("Log in Here"); 
        a.appendChild(link);  
        a.title = "Log in Here";  
        a.href = loginUrl;  
        document.body.appendChild(a);
      } else if (status[0].localeCompare("true") === 0) {
        console.log("url: " + logoutUrl);
        commForm.style.visibility = "visible";
        // Create the logout URL
        var a = document.createElement('a');  
        var link = document.createTextNode("Log out Here"); 
        a.appendChild(link);  
        a.title = "Log out Here";  
        a.href = logoutUrl;  
        document.body.appendChild(a);
      } 
    });
}
