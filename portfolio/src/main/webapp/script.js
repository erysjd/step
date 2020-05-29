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

/**
 * Fetches a message from the server and adds it to the DOM.
 */
function getMessage() {
  console.log('Fetching a message.');

  // The fetch() function returns a Promise because the request is asynchronous.
  const responsePromise = fetch('/data');

  // When the request is complete, pass the response into handleResponse().
  responsePromise.then(handleResponse);
}

/**
 * Handles response by converting it to text and passing the result to
 * addMessageToDom().
 */
function handleResponse(response) {
  console.log('Handling the response.');

  // response.text() returns a Promise, because the response is a stream of
  // content and not a simple variable.
  const textPromise = response.text();

  // When the response is converted to text, pass the result into the
  // addMessageToDom() function.
  textPromise.then(addMessageToDom);
}

/** Adds a message to the DOM. */
function addMessageToDom(message) {
  console.log('Adding message to dom: ' + message);

  const messageContainer = document.getElementById('message-container');
  messageContainer.innerText = message;

  /**
 * Fetches messages from the servers and adds them to the DOM.
 */
function getMessagess() {
  fetch('/data').then(response => response.json()).then((message) => {
      document.getElementById('message-container').innerText = message;
  });
}