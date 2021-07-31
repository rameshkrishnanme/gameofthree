var ws;

var host = document.location.host;
var pathname = document.location.pathname;
var playground  = "";

function connect() {
    var username = document.getElementById("username").value;
    var opponent = document.getElementById("opponent").value;
    
    ws = new WebSocket("ws://" +host  + pathname + "game/" + username);

	var username = document.getElementById("username").disabled;

    ws.onmessage = function(event) {
        console.log(event.data);
        var message = JSON.parse(event.data);
        
        if (message.status == 100) { // Joined
        	log.innerHTML = "";
			log.innerHTML += message.message +  " - Starting Point  " + message.point + "\n";
			playground = message.playground;
			
			document.getElementById("joinagame").disabled = true;
			document.getElementById("connect").disabled = true;
			document.getElementById("game-1").disabled = false;
       		document.getElementById("game-2").disabled = false;
       		document.getElementById("game-3").disabled = false;
		} 
		if (message.status == 500) { // Not a your turn
       		log.innerHTML += message.message + " - Current Point  " + message.point + "\n";
        }
        if (message.status == 300) { // Response
        	log.innerHTML += "\n";
        	log.innerHTML += "                   "  + message.message + " - Current Point  " + message.point + "\n";
        	log.innerHTML += "                   [ "  +  message.calculation + " ]\n";
        }
        if (message.status == 700) { // Playing Response
        	log.innerHTML += "\n";
        	log.innerHTML += message.message + " - Current Point  " + message.point + "\n";
       		log.innerHTML += message.calculation + "\n";
        }
        if (message.status == 400) { // Completed
        	log.innerHTML += "\n";
       		log.innerHTML += "Game Result -  " + message.message + " - Current Point  " + message.point + "\n";
       		log.innerHTML += +message.calculation + "\n";
       		log.innerHTML += "\n";
       		log.innerHTML += "You can join the New Game";
       		gameStarted = false;
       		
       		document.getElementById("joinagame").disabled = false;
       		document.getElementById("game-1").disabled = true;
       		document.getElementById("game-2").disabled = true;
       		document.getElementById("game-3").disabled = true;
        }
        
        logTa = document.getElementById("log")
        logTa.scrollTop = logTa.scrollHeight;
        
    };
}

function joinAGame() {
     var json = JSON.stringify({
		"message" : document.getElementById("opponent").value,
		"status":100
    });
    ws.send(json);
    gameStarted = true;
}

function sendAPoint(value) {
    var json = JSON.stringify({
		"status":300,
		"playground": playground,
		"point":value
    });
    ws.send(json);
}
var gameStarted = false;

function refreshPlayers() {
	
	if (gameStarted) {
		return;
	}
	
	const xhttp = new XMLHttpRequest();
	const url='http://' + host + pathname + "/users";
	xhttp.open("GET", url);
	xhttp.send();
	
	
	xhttp.onreadystatechange = function() {
	     if (this.readyState == 4 && this.status == 200) {
			  console.log(xhttp.responseText)
			  var select = document.getElementById("opponent");
			  for (var i = select.options.length - 1 ; i >= 0 ; i--)
        	  	select.remove(i);
			   
			  var options = JSON.parse(xhttp.responseText);
			  for(var i = 0; i < options.length; i++) {
				    var opt = options[i];
				    var el = document.createElement("option");
				    el.textContent = opt;
				    el.value = opt;
				    select.appendChild(el);
			   }
	     }
	};	
}

setTimeout(refreshPlayers, 700);
setInterval(refreshPlayers, 7000);