function randomNumber() {
   return Math.floor(Math.random());
}

document.body.innerHTML = "<h1>El numero de tu suerte de hoy es: " + randomNumber() + "</h1>";