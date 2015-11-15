# PluginForApp 
El plugin funciona tanto para VideoView como para MediaPlayer.

VideoView:
En este caso, he tenido que implementar un "CustomVideoView", en el cual he añadido los listeners de "onPlay()" y de "onPause"
porque no se me ha ocurrido ninguna otra manera de poder detectar cuando el usuario le da al play o al pause.

Para usar el plugin se debe declarar una instancia de la clase creada por mi "MyPlugin", donde en la misma constructora ya declaro
los listeners para que automáticamente muestre por pantalla, mediante un "Toast" , el numero de "pauses" que llevas al tocar el
botón de pause, y el numero de "plays" junto con el "Elapsed Time" entre el último pause y el actual play al tocar el botón de play.

Además la primera vez que se le da al play se realiza la "HTTP Request fake" que se pide en el enunciado, también realiza otro 
"HTTP Request fake" al finalizar el vídeo, además de mostrar los "plays" y "pauses" que se han contado hasta el momento, no he implementado
el "HTTP Request fake" cuando se envía el primer frame porque no he encontrado un listener para los videoview que me indique tal cosa.

Los "listeners" no interfieren con los de la aplicación porque en la aplicación no hay implementado ningún "listener" de los que
uso en el plugin, en caso de que también estuvieran en la aplicación, creo que si interferirían con ellos, pero es algo que
no se solucionar.

MediaPlayer:
En este otro caso, he tenido que hacer lo mismo que para el videoview pero también puedo enviar el "HTTP Request fake" cuando se envía
el primer "frame" gracias al listener "setOnBufferingUpdateListener" que no tenia en el videoview.


El plugin lo he implementado como una clase más del mismo proyecto para poder probar que lo que hacía estaba bien, también he creado
una librería android en otro proyecto pero como no he conseguido exportarlo/importarlo en la aplicación principal he decidido volver a
implementarlo como una clase más del proyecto.
