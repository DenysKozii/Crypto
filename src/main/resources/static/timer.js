setInterval(function () {
    if (document.location.href === "http://localhost:8080/") {
        document.location.href = "http://localhost:8080/";
    }
}, 1000);