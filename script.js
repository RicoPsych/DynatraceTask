const url = "http://localhost:8888/exchanges/"

window.addEventListener('load', () => {
    const diff_submit = document.getElementById('diff');
    diff_submit.addEventListener('submit', event => event.preventDefault() );
});

function diplayResult(text) {
    let element = document.getElementById("result");
    if(text.length != 2){
        element.textContent = text;
    }
    else{
        element.textContent = "Min: " + text[0];
        element.textContent += "\nMax: " + text[1];
    }
}
function getParameterById(id){
    let element = document.getElementById(id);
    return element.value;
}

function getAvgOnDay(){    
    let currencyCode = getParameterById("curr");
    let amount = getParameterById("date");
    sendQuery( url + currencyCode + "/" + amount); 
}

function getMinMax(){    
    let currencyCode = getParameterById("curr");
    let amount = getParameterById("amount");
    sendQuery( url + currencyCode + "/minmax/" + amount); 
}

function getMajorDifference(){    
    let currencyCode = getParameterById("curr");
    let amount = getParameterById("amount");
    sendQuery( url + currencyCode + "/diff/" + amount); 
}
function sendQuery(uri){
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            diplayResult(JSON.parse(this.responseText));
        }
        if (this.readyState === 4 && this.status === 404) {
            alert("Not found");
        }
        if (this.readyState === 4 && this.status === 400) {
            alert("Bad request");
        }
    };

    xhttp.open("GET",uri , true);
    xhttp.send();
}




