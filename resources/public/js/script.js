function randomNumber() {
   return Math.floor(Math.random()*100);
}

document.body.innerHTML = "<h1 style='color:white'>El numero de tu suerte de hoy es: " + randomNumber() + "</h1>";