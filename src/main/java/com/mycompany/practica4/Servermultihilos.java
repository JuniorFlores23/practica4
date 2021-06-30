/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.practica4;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author junior
 */
public class Servermultihilos {
    
    public static void main(String[] args) throws IOException {
      //creamos el socket con su puerto  
      ServerSocket server = new ServerSocket(5003);
      try{
            server.setReuseAddress(true);
            JOptionPane.showMessageDialog(null,"SERVER ACTIVO...");
      //la clase principal acepta nuevas conexiones
      while(true){
            Socket client = server.accept();//aceptamos al nuevo cliente
            System.out.println("NUEVO CLIENTE CONECTADO = "+client.getInetAddress().getCanonicalHostName());//obtenemos el hostname
            ClientHandler clientSock = new ClientHandler(client); //creamos un objeto de la clase ClientHandler
          
             //cada hilo se va a ejecutar por separado
            new Thread(clientSock).start(); //cada hilo se le pasara como parametro el socket del cliente
      }
      
    }catch(IOException e){
          System.out.println("ERROR ->"+e.getMessage());
    }finally{
          if(server != null){
              try{
                  server.close(); //en caso de ocurra alguna excepcion cerramos la conexion
              }catch(IOException e){
              }
          }
      }
    }
    private static class ClientHandler implements Runnable{
        
        private final Socket clientSocket;
        public ClientHandler(Socket socket){
            this.clientSocket = socket; //accedemos al valor del socket por medio de this
        }
        
        @Override
        public void run(){ //metodo que se implementa de la interfaz Runnable
            DataInputStream in; // dato de tipo entrada
            int r;
            try{
            in = new DataInputStream(clientSocket.getInputStream()); //creamos un nuevo dato de entrada stream
            //out = new DataOutputStream(sc.getOutputStream());
            
            //leemos los que nos mande el cliente
            String mensaje = in.readUTF();
            //obtenemos el nombre del archivo que el cliente quiere
            System.out.println("EL ARCHIVO QUE BUSCA ES :"+mensaje);
            if(mensaje!=null)//mientras el cliente haya mandado algo
            {
                FileInputStream fin = new FileInputStream("/home/junior/server/"+mensaje);//concatamos el nombre del archivo junto a la direccion que tendra
                                                                                         //el nuevo dato de tipo filestream de entrada
                DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream()); //asignamos una variable de salida de datos que le enviaremos al cliente
                while((r= fin.read())!= -1){
                    dout.write(r); //hacemos lectura en el while y escribimos los datos en la variable de salida que se le envia al cliente
                }
                dout.flush(); //limpiamos el buffer
                System.out.println("ARCHIVO ENVIADO");
                clientSocket.close();//cerramos conexion
                System.out.println("CLIENTE DESCONECTADO = "+clientSocket.getInetAddress().getCanonicalHostName());//obtenemos el hostname
                dout.flush();//limpiamos el buffer de tipo out
            }
            }catch(IOException e){
                //en caso de que no se haya encontrado el archivo nos lanzara una excepcion
                //que capturamos en este catch
                try {
                    clientSocket.close();//cerramos conexion
                    System.out.println("CLIENTE DESCONECTADO = "+clientSocket.getInetAddress().getCanonicalHostName());
                } catch (IOException ex) {
                  Logger.getLogger(Servermultihilos.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("ERROR ->"+e.getMessage());
            }
        }
        
    }
}
  //obtene el nombre de los archivos de un directorio
        /*final String NOMBRE_DIRECTORIO = "/home/elias/Documentos";
        //obtener el nombre de los archivos que contenga algun directorio
        try(DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(NOMBRE_DIRECTORIO))){
        
            for(Path ruta: ds){
                //JOptionPane.showMessageDialog(null,ruta.getFileName());
                System.out.println(ruta.getFileName());
            }
        } catch(IOException e){
            System.err.println("ERROR -> "+ e.getMessage());
        }//fin del try catch*/