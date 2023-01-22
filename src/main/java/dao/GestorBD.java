/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import beans.Autor;
import beans.Libro;

/**
 *
 * @author Amaia
 */
public class GestorBD {
	private static DataSource dataSource;

    public GestorBD() {
        try {
            InitialContext ctx = new InitialContext();
            Context env = (Context) ctx.lookup("java:comp/env");
            // nombre del recurso en el context.xml
            dataSource = (DataSource) env.lookup("jdbc/poolBibliotecaDB");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<Libro> libros(){
        ArrayList<Libro> libros = new ArrayList<Libro>();
        String sql = "SELECT * FROM libro";
        try {
            Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                Libro libro = new Libro(rs.getInt("id"), rs.getString("titulo"),
                                        rs.getInt("paginas"), rs.getString("genero"), 
                                        rs.getInt("idAutor"));
                libros.add(libro);
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("Error en metodo libros: " + ex);
        }
        return libros;
    }
    
    public LinkedHashMap<Integer, String> autores(){
        LinkedHashMap<Integer, String> autores = new LinkedHashMap<Integer, String>();
        String sql = "SELECT id, nombre FROM autor";
        Connection con;
        try {
            con = dataSource.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                autores.put(rs.getInt("id"), rs.getString("nombre"));
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return autores;
    }
    
    public boolean existeLibro(Libro libro){
        boolean existe = false;
        String sql = "SELECT id FROM libro WHERE titulo = '" + libro.getTitulo() +
                "' AND idautor = " + libro.getIdAutor();
        try {
            Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                existe = true;
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return existe;
    }
    
    public int insertarLibro(Libro libro){
        int id = -1;
        String sql = "INSERT INTO libro(titulo, paginas, genero, idautor) "
                + " VALUES(?, ?, ?, ?)";
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, libro.getTitulo());
            st.setInt(2, libro.getPaginas());
            st.setString(3, libro.getGenero());
            st.setInt(4, libro.getIdAutor());
            
            st.executeUpdate();
            
            ResultSet rs = st.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
            
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("Error en metodo insertarLibro: " + ex);
        }
        
        return id;
    }
    
    
    public ArrayList<Autor> autorCompleto(){
        ArrayList<Autor> arrAut= new ArrayList<Autor>();
        String sql = "SELECT * from autor";
        try {
            Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                Autor a= new Autor();
                int id=rs.getInt("id");
                String nombre=rs.getString("nombre");
                String nacionalidad=rs.getString("nacionalidad");
                Date fechanac=rs.getDate("fechanac");
                a.setFechanac(fechanac);
                a.setIdAutor(id);
                a.setNacionalidad(nacionalidad);
                a.setNombre(nombre);
                arrAut.add(a);
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrAut;
    }
    
    public ArrayList<Libro> librosAutor(String autor){
        ArrayList<Libro> arrLib= new ArrayList<Libro>();
        String sql = "SELECT * from libro where idautor="+autor;
        try {
            Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                Libro l= new Libro();
                int idLibro=rs.getInt("id");
                String titulo=rs.getString("titulo");
                int paginas=rs.getInt("paginas");
                String genero=rs.getString("genero");
                int idAutor=rs.getInt("idAutor");
                l.setGenero(genero);
                l.setIdAutor(idAutor);
                l.setIdLibro(idLibro);
                l.setPaginas(paginas);
                l.setTitulo(titulo);
                arrLib.add(l);
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrLib;
    }
    
    public String fromIdToName(String autor){
        String sql = "SELECT * from autor where id="+autor;
        String nombre="Autor";
        try {
            Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                nombre=rs.getString("nombre");
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nombre;
    }
    public int idPrestamoMayor(){
        String sql = "select id from prestamo where id=(select max(id) from prestamo)";
        int idMax=0;
        try {
            Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
            	idMax=rs.getInt("id")+1;
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idMax;
    }
    public void reservarLibro(String id){
    	int idMax=idPrestamoMayor();
        String sql = "insert into prestamo values("+idMax+",sysdate(),"+id+")";
        try {
            Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String autorExistente(String nombre) {
    	String sql = "select count(*) from autor where nombre='"+nombre+"'";
        int idMax=0;
        try {
            Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
            	idMax=rs.getInt("count(*)");
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    	if(idMax>0) {
    		return "true";
    	}
    	else {
    		return "false";
    	}
    }
    public int idAutorMayor(){
        String sql = "select id from autor where id=(select max(id) from autor)";
        int idMax=0;
        try {
            Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
            	idMax=rs.getInt("id")+1;
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idMax;
    }
    //no me va lo de las fechas
    public void aniadirAutor(String nombre, String fecha, String nacionalidad){
    	int id=idAutorMayor();
//        SimpleDateFormat fechaFormat=new SimpleDateFormat("dd-MMM-yyyy");  
		try {
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//
//			String dateString = format.format( new Date()   );
//			Date   date       = format.parse ( "2009-12-31" );
			String sql = "insert into autor values("+id+","+nombre+","+fecha+","+nacionalidad+")";
	        int idMax=0;
            Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            
            st.close();
            con.close();
		
        } catch (SQLException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
//		catch (ParseException e) {
//	        e.printStackTrace();
//		}
    }

	public LinkedHashMap<String, Integer> librosPrestados() {
		LinkedHashMap<String, Integer> librosPrestados= new LinkedHashMap<String, Integer>();
		String sql = "select libro.titulo, prestamo.fecha from prestamo, libro where libro.id=prestamo.idlibro group by libro.titulo order by prestamo.fecha";
        try {
            Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            Date fechaactual = new Date(System.currentTimeMillis());
            int milisecondsByDay = 86400000;
            while(rs.next()){
            	String nombre=rs.getString("libro.titulo");
            	Date fecha= rs.getDate("prestamo.fecha");
            	int dias = (int) ((fechaactual.getTime()-fecha.getTime()) / milisecondsByDay);
            	librosPrestados.put(nombre, dias);
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return librosPrestados;
	}
	
	public void borrarLibros(String arrLibro) {
		String sql = "delete FROM prestamo WHERE idlibro = (select id from libro where titulo=?);";
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, arrLibro);
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            
           
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
//	public int insertarLibro(Libro libro){
//        int id = -1;
//        String sql = "INSERT INTO libro(titulo, paginas, genero, idautor) "
//                + " VALUES(?, ?, ?, ?)";
//        try {
//            Connection con = dataSource.getConnection();
//            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            st.setString(1, libro.getTitulo());
//            st.setInt(2, libro.getPaginas());
//            st.setString(3, libro.getGenero());
//            st.setInt(4, libro.getIdAutor());
//            
//            st.executeUpdate();
//            
//            ResultSet rs = st.getGeneratedKeys();
//            if(rs.next()){
//                id = rs.getInt(1);
//            }
//            
//            rs.close();
//            st.close();
//            con.close();
//        } catch (SQLException ex) {
//            System.err.println("Error en metodo insertarLibro: " + ex);
//        }
//        
//        return id;
//    }
}
