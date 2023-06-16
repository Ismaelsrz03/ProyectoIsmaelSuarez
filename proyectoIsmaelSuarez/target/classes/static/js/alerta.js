 setTimeout(function() {
      document.querySelector('.alerta').style.display = 'none';
    }, 5000); // La alerta desaparecerá después de 5 segundos (5000 milisegundos)
    
    document.addEventListener("DOMContentLoaded",()=>{
		document.title="Invincible body";
		var iconLink = document.querySelector("link[rel~='icon']");



if (!iconLink) {

iconLink = document.createElement("link");

iconLink.rel = "icon";

document.head.appendChild(iconLink);

}



iconLink.href = "/Imagenes/iconoLogo.png";

	});
	
	