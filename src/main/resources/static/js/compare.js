function start() {
   disableSelection(["select-1", "select-2", "select-3", "select-4", "select-5", "one-by-one", "batch"]);
   disableStart("start-btn");
   clearResults();
   let req = getRequestType();
   console.log(req);
//   Promise.all([
//     startCompare("http_10k", req, Number(document.getElementById("select-1").value), "http");
//     startCompare("db_10k", req, Number(document.getElementById("select-1").value), "db");
//     startCompare("http_50k", req, Number(document.getElementById("select-2").value), "http");
//     startCompare("db_50k", req, Number(document.getElementById("select-2").value), "db");
//     startCompare("http_100k", req, Number(document.getElementById("select-3").value), "http");
//     startCompare("db_100k", req, Number(document.getElementById("select-3").value), "db");
//     startCompare("http_500k", req, Number(document.getElementById("select-4").value), "http");
//     startCompare("db_500k", req, Number(document.getElementById("select-4").value), "db");
//     startCompare("http_1m", req, Number(document.getElementById("select-5").value), "http");
//     startCompare("db_1m", req, Number(document.getElementById("select-5").value), "db");
//   ]).then(() => {
//     enableSelection(["select-1", "select-2", "select-3", "select-4", "select-5", "one-by-one", "batch"]);
//     enableStart("start-btn");
//   });
     Promise.resolve()
        .then(function() {
            return startCompare("http_10k", req, Number(document.getElementById("select-1").value), "http");
        })
        .then(function() {
            return startCompare("db_10k", req, Number(document.getElementById("select-1").value), "db");
        })
        .then(function() {
            return startCompare("http_50k", req, Number(document.getElementById("select-2").value), "http");
        })
        .then(function() {
            return startCompare("db_50k", req, Number(document.getElementById("select-2").value), "db");
        })
        .then(function() {
            return startCompare("http_100k", req, Number(document.getElementById("select-3").value), "http");
        })
        .then(function() {
            return startCompare("db_100k", req, Number(document.getElementById("select-3").value), "db");
        })
        .then(function() {
            return startCompare("http_500k", req, Number(document.getElementById("select-4").value), "http");
        })
        .then(function() {
            return startCompare("db_500k", req, Number(document.getElementById("select-4").value), "db");
        })
        .then(function() {
            return startCompare("http_1m", req, Number(document.getElementById("select-5").value), "http");
        })
        .then(function() {
            return startCompare("db_1m", req, Number(document.getElementById("select-5").value), "db");
        })
        .then(function() {
            enableSelection(["select-1", "select-2", "select-3", "select-4", "select-5", "one-by-one", "batch"]);
            enableStart("start-btn");
        });
}

function getRequestType() {
    if (document.getElementById("batch").checked) {
        return "startBatch";
    } else if (document.getElementById("one-by-one").checked) {
        return "start";
    }
    return "unsupported";
}

function disableStart(btnId) {
   document.getElementById(btnId).disabled = true;
}

function disableSelection(selectionIds) {
   for (let id of selectionIds) {
      document.getElementById(id).setAttribute("disabled", "disabled");
   }
}

function enableStart(btnId) {
   document.getElementById(btnId).disabled = false;
}

function enableSelection(selectionIds) {
   for (let id of selectionIds) {
      document.getElementById(id).removeAttribute("disabled");
   }
}

function clearResults() {
    document.getElementById("db_10k").innerHTML = "";
    document.getElementById("http_10k").innerHTML = "";
    document.getElementById("db_50k").innerHTML = "";
    document.getElementById("http_50k").innerHTML = "";
    document.getElementById("db_100k").innerHTML = "";
    document.getElementById("http_100k").innerHTML = "";
    document.getElementById("db_500k").innerHTML = "";
    document.getElementById("http_500k").innerHTML = "";
    document.getElementById("db_1m").innerHTML = "";
    document.getElementById("http_1m").innerHTML = "";
}

async function startCompare(idCell, request, numRecords, requestType) {
  const response = await fetch(`/${request}?numRecords=${numRecords}&requestType=${requestType}`);
  const res = await response.text();
  if (response.ok) {
    document.getElementById(idCell).innerHTML = res;
  } else {
    window.location.replace("/error");
    console.log("replaced");
  }
  return res;
}

function changeCompareType(radio) {
    if (radio.value === "One-by-one") {
       document.getElementById("title").innerHTML = "One by one comparison"
    } else if (radio.value == "Batch") {
       document.getElementById("title").innerHTML = "Batch comparison"
    }
}

function updateRadio() {
    if (document.getElementById("one-by-one").checked) {
        document.getElementById("title").innerHTML = "One by one comparison"
    } else if (document.getElementById("batch").checked) {
        document.getElementById("title").innerHTML = "Batch comparison"
    }
}