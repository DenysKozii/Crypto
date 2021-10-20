setInterval(function () {
    if (document.location.href === "http://localhost:8085/") {
        document.location.href = "http://localhost:8085/";
    }
}, 1000);