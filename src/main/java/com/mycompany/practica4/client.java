/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.practica4;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
/**
 *
 * @author junior
 */
public class client {
    
    public static void main(String[] argv)
    {
        final String dir;
        final int puerto = 5003;
        DataOutputStream out;
        //DataInputStream in;
        int r;
        try {    
        //pedimos la direccion del servidor    
        dir = JOptionPane.showInputDialog("INGRESE LA DIRECCION IP DEL SERVIDOR:");
        //creamos el socket en el que se conectara el cliente
        Socket sr = new Socket(dir,puerto);
        if(sr.isConnected())//mientras haya conexion
        {
            System.out.println("CONECTADO AL SERVIDOR");
            //in = new DataInputStream(sr.getInputStream());
            out = new DataOutputStream(sr.getOutputStream());
            //enviamos lo que queremos descargar
            String arch = JOptionPane.showInputDialog("INGRESE EL NOMBRE DEL ARCHIVO CON SU EXTENSION:");
            //le enviamos la cadena en un DataOutputStream
            out.writeUTF(arch);
            out.flush();//limpiamos el buffer
            //recibimos desde el servidor 
            DataInputStream din = new DataInputStream(sr.getInputStream()); //para comprobar de que no existe el archivo
            BufferedInputStream buff = new BufferedInputStream(din);
            //String retur = din.toString();
            //en caso de que nos retorne el catch del server bytes con valores menores a 1 
            JOptionPane.showMessageDialog(null, din.available());
            if(din.available() < 1)
            {
                //este condicional ocurre cuando no se encuentra el archivo
                JOptionPane.showMessageDialog(null,"ARCHIVO NO ENCONTRADO\n\n CERRANDO CLIENTE...");
                sr.close();
            }else{
                //en caso de que se encuentre el archivo
                DataInputStream din2 = new DataInputStream(sr.getInputStream()); //declaramos una varible de entrada de stream
                FileOutputStream fout = new FileOutputStream("/home/junior/Documentos/"+arch);//declaramos una variable de salida
                while((r=din2.read())!= -1) //leemos el archivo mientras contenga bytes
                {
                    fout.write((char)r);//escribe en el file de salida
                }
                fout.flush(); //limpiamos el buffer
                JOptionPane.showMessageDialog(null,"ARCHIVO DESCARGADO");//mandamos a imprimir
                sr.close();// cerramos la conexion
            }
        
        }
        }catch (IOException e){
            System.err.println("ERROR ->"+e.getMessage());
        }
        
    }
}
