// Get the DOM elements
const modal = document.getElementById('config-modal');
const configButton = document.getElementById('config-button');
const closeBtn = document.querySelector('.close');
const mainForm = document.getElementById('scan-form');
const ltForm = document.getElementById('lt-data-form');
const performanceRadio = document.getElementById('performance-radio');
const responseTimeRadio = document.getElementById('response-time-radio');
const methodSelect = document.getElementById('method-select');
const protocolRadio = document.getElementById('protocol-radio');

// Define the initial configuration data
const data = {
  url: '',
  method: '',
  body: {
    users: '',
    loadTime: '',
    loopCount: '',
  },
};

// Show modal on button click
configButton.addEventListener('click', function() {
  modal.style.display = 'block';
});

// Close modal on close button click
closeBtn.addEventListener('click', function() {
  modal.style.display = 'none';
});

// Close modal on outside click
window.addEventListener('click', function(event) {
  if (event.target === modal) {
    modal.style.display = 'none';
  }
});

// Handle main form submission
mainForm.addEventListener('submit', function(e) {
  e.preventDefault();

  // Set up the current input data
  data.url = document.getElementById('url-input').value;
  data.method = document.getElementById('method-select').value;

  // Get the options list element
  const optionsList = document.getElementById('options-list');

  // Get the currently checked input element
  const checkedInput = optionsList.querySelector('input:checked');

  // Get the value of the currently checked input
  const checkedValue = checkedInput.value;

  if (checkedValue === 'response-time') {
    getResponseTime(data);
  } else if (checkedValue === 'protocol') {
    getURLProtocol(data);
  } else {
    getLTResponseTime(data);
  }
});

// Handle load test form submission
ltForm.addEventListener('submit', function(e) {
  e.preventDefault();

  // Get the form data
  const users = document.getElementById('users').value;
  const loadTime = document.getElementById('load-time').value;
  const loopCount = document.getElementById('loop-count').value;

  // Set the form data to the configuration data
  data.body = {
    users,
    loadTime,
    loopCount,
  };

  // Close the modal
  modal.style.display = 'none';
});

// Hide/show the method select depending on the selected radio button
function handleRadioChange() {
  methodSelect.style.display = performanceRadio.checked || responseTimeRadio.checked ? 'block' : 'none';
}

// Add event listeners to the radio buttons and protocol radio
performanceRadio.addEventListener('change', handleRadioChange);
responseTimeRadio.addEventListener('change', handleRadioChange);
protocolRadio.addEventListener('change', function() {
  methodSelect.style.display = 'none';
});

// Getting the response time of a simple single-user request
function getResponseTime(data) {
  const xhr = new XMLHttpRequest();
  xhr.open('POST', baseUrl + 'getResponseTime');
  xhr.setRequestHeader('Content-Type', 'application/json');
  xhr.onload = function() {
    // Handle the response from the server
    console.log(xhr.responseText);
  };
  const requestData = {
    url: data.url,
    requestMethod: data.method,
  };
  xhr.send(JSON.stringify(requestData));
}

// Getting the URL protocol type
function getURLProtocol(data) {
  const xhr = new XMLHttpRequest();
  xhr.open('POST', baseUrl + 'scanUrlForProtocol');
  xhr.setRequestHeader('Content-Type', 'application/json');
  xhr.onload = function(){
    // Handle the response from the server
    console.log(xhr.responseText);
  };
  const requestData = {
    url: data.url,
  };
  xhr.send(JSON.stringify(requestData));
}

// Getting the load test response time
function getLTResponseTime(data){
    const xhr = new XMLHttpRequest();
    xhr.open("POST", baseUrl + 'scanLocalhostForPerformance');
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onload = function() {
    // Handle the response from the server
        console.log(xhr.responseText);
    };
    const requestData = {
        url: data.url,
        requestMethod: data.method,
        users: data.body.users,
        timeToLoadAllUsers: data.body.loadTime,
        loopCount: data.body.loopCount
    };
    xhr.send(JSON.stringify(requestData));
}